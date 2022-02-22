package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.permisos.AgregarQuitarRequest;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface PermisoService {

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;

    RespuestaGenerica findBySubmoduloIdAndEsActivo(Integer id, Boolean activo) throws ServiceException;

    RespuestaGenerica agregar(AgregarQuitarRequest request) throws ServiceException;

    RespuestaGenerica quitar(AgregarQuitarRequest request) throws ServiceException;

    RespuestaGenerica findByRol(Integer rolId, Boolean esActivo) throws ServiceException;

}
