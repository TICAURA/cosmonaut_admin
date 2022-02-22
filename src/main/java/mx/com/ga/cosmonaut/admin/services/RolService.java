package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.roles.GuardarRequest;
import mx.com.ga.cosmonaut.common.dto.administracion.roles.ModificarRequest;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface RolService {

    RespuestaGenerica guardar(GuardarRequest request) throws ServiceException;

    RespuestaGenerica modificar(ModificarRequest request) throws ServiceException;

    RespuestaGenerica eliminar(Integer rolId) throws ServiceException;

    RespuestaGenerica obtenerId(Integer rolId) throws ServiceException;

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;

    RespuestaGenerica findByCentrocClientesCentrocClienteIdAndEsActivo(Integer id, Integer idVersion, Boolean activo)
            throws ServiceException;

    RespuestaGenerica listByRolIdRolIdAndCentrocClienteId(Integer id,Integer idVersion, Integer idRol)
            throws ServiceException;

}
