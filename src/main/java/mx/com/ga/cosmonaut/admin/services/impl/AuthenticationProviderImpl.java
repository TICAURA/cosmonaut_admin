package mx.com.ga.cosmonaut.admin.services.impl;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmUsuarios;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmEstatusUsuariosChatRepository;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmUsuariosRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;
import mx.com.ga.cosmonaut.common.util.Utilidades;
import org.mindrot.jbcrypt.BCrypt;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Optional;

@Singleton
public class AuthenticationProviderImpl implements AuthenticationProvider {

    @Inject
    private AdmUsuariosRepository admUsuariosRepository;

    @Inject
    private AdmEstatusUsuariosChatRepository admEstatusUsuariosChatRepository;

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        Optional<AdmUsuarios> usuario = admUsuariosRepository.findByEmail(authenticationRequest.getIdentity().toString());

        if (usuario.isPresent()) {
            if (!usuario.get().isEsActivo()) {
                return Flowable.create(emitter -> emitter.onError(new AuthenticationException(
                        new AuthenticationFailed("El usuario se encuentra dado de baja actualmente."))), BackpressureStrategy.ERROR);
            }

            if (Utilidades.isNotValidPwdDate(usuario.get().getFechaUltimoPassword())) {
                return Flowable.create(emitter -> emitter.onError(new AuthenticationException(
                        new AuthenticationFailed("La contraseña ha expirado"))), BackpressureStrategy.ERROR);
            }

            if(!usuario.get().getRolId().isEsActivo()){
                return Flowable.create(emitter -> emitter.onError(new AuthenticationException(
                        new AuthenticationFailed("Usuario sin accesso al sistema, rol de usuario inactivo"))), BackpressureStrategy.ERROR);

            }

            return Flowable.create(emitter -> {
                if (BCrypt.checkpw(authenticationRequest.getSecret().toString(), usuario.get().getPassword())) {
                    emitter.onNext(new UserDetails((String) authenticationRequest.getIdentity(), new ArrayList<>()));
                    emitter.onComplete();
                } else {
                    emitter.onError(new AuthenticationException(new AuthenticationFailed("Crdenciales invalidas")));
                }
            }, BackpressureStrategy.ERROR);
        } else {
            return Flowable.create(emitter -> emitter.onError(new AuthenticationException(
                    new AuthenticationFailed("Credenciales invalidas"))), BackpressureStrategy.ERROR);
        }
    }
}
