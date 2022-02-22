package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface ModuloService {

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;

    RespuestaGenerica findByVersion(Integer id, Boolean activo) throws ServiceException;

    RespuestaGenerica findURL(Integer idUsuario, Integer idCliente, Integer version) throws ServiceException;

}
