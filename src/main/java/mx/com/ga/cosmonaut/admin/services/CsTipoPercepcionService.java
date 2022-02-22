package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTipoPercepcion;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CsTipoPercepcionService {

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;

    RespuestaGenerica findByEsActivoPeriodicidad(Boolean activo, String tipoPeriodicidad) throws ServiceException;

    RespuestaGenerica modificar(CsTipoPercepcion csTipoPercepcion) throws ServiceException;
    
    RespuestaGenerica guardar(CsTipoPercepcion csTipoPercepcion) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;

}
