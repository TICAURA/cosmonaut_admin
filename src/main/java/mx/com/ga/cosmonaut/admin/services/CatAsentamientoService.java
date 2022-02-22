
package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.ubicacion.CatAsentamiento;
import mx.com.ga.cosmonaut.common.exception.ServiceException;


public interface CatAsentamientoService {

    RespuestaGenerica findByCodigoAndEsActivo(String codigo) throws ServiceException;

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;
    
    RespuestaGenerica modificar(CatAsentamiento asentamiento) throws ServiceException;
    
    RespuestaGenerica guardar(CatAsentamiento asentamiento) throws ServiceException;
}
