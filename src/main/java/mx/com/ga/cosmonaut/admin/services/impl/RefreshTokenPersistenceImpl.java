package mx.com.ga.cosmonaut.admin.services.impl;

import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.RefreshTokenEntity;
import mx.com.ga.cosmonaut.common.service.FirestoreService;
import mx.com.ga.cosmonaut.common.util.Utilidades;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Singleton
public class RefreshTokenPersistenceImpl implements RefreshTokenPersistence {

    private static final Logger LOG = LoggerFactory.getLogger(RefreshTokenPersistenceImpl.class);

    @Inject
    private FirestoreService firestoreService;

    @Override
    @EventListener
    public void persistToken(RefreshTokenGeneratedEvent event) {
        if (event != null && event.getRefreshToken() != null && event.getUserDetails() != null
                && event.getUserDetails().getUsername() != null) {
            try {
                RefreshTokenEntity rf = firestoreService.obtener(event.getUserDetails().getUsername());
                if (rf != null) {
                    firestoreService.update(rf.getUsername(), event.getRefreshToken());
                } else {
                    rf = new RefreshTokenEntity();
                    rf.setRefresh_token(event.getRefreshToken());
                    rf.setDate_created(Utilidades.obtenerFechaSystema());
                    rf.setUsername(event.getUserDetails().getUsername());
                    firestoreService.guardar(rf);
                }
            } catch (InterruptedException | ExecutionException ex) {
                Thread.currentThread().interrupt();
                LOG.error("Error al persistir refresh token: " +ex.getMessage());
            }
        }
    }

    @Override
    public Publisher<UserDetails> getUserDetails(String refreshToken) {
        return Flowable.create(emitter -> {
            RefreshTokenEntity rf = firestoreService.buscar(refreshToken);
            if (rf != null) {
                emitter.onNext(new UserDetails(rf.getUsername(), new ArrayList<>()));
                emitter.onComplete();
            } else {
                emitter.onError(new OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "Refresh token revocado", null));
            }
        }, BackpressureStrategy.ERROR);
    }

}
