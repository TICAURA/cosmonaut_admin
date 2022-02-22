package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface SubmoduloService {

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;

}
