package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.AuthService;
import mx.com.ga.cosmonaut.admin.services.BIService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmUsuarios;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmEstatusUsuariosChatRepository;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmUsuariosRepository;
import mx.com.ga.cosmonaut.common.service.BigQueryService;
import mx.com.ga.cosmonaut.common.service.FirestoreService;
import mx.com.ga.cosmonaut.common.util.Constantes;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class BIServiceImpl implements BIService {

    @Inject
    private BigQueryService bigQueryService;

    @Override
    public RespuestaGenerica test() throws ServiceException {
        bigQueryService.crearTablas();
        bigQueryService.insertarRegistros();
        bigQueryService.consultaCommits();
        return null;
    }

}
