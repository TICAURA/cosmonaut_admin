package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.AuthService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmUsuarios;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmEstatusUsuariosChatRepository;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmUsuariosRepository;
import mx.com.ga.cosmonaut.common.service.FirestoreService;
import mx.com.ga.cosmonaut.common.util.Constantes;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class AuthServiceImpl implements AuthService {

    @Inject
    private FirestoreService firestoreService;

    @Inject
    private AdmUsuariosRepository admUsuariosRepository;

    @Inject
    private AdmEstatusUsuariosChatRepository admEstatusUsuariosChatRepository;

    @Override
    public RespuestaGenerica logout(String username) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            Optional<AdmUsuarios> admUsuarios = admUsuariosRepository.findByEmail(username);
            if (admUsuarios.isEmpty()) {
                respuestaGenerica.setResultado(Constantes.RESULTADO_ERROR);
                respuestaGenerica.setMensaje(Constantes.ERROR_USUARIO_NO_EXISTE);
            } else {

                firestoreService.borrar(username);

                respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
                respuestaGenerica.setMensaje(Constantes.EXITO);
            }
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" logout" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

}
