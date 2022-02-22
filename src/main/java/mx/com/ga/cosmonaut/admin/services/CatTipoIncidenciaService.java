package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatTipoIncidencia;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CatTipoIncidenciaService {
    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;
    
    RespuestaGenerica modificar(CatTipoIncidencia catTipoIncidencia) throws ServiceException;
    
    RespuestaGenerica guardar(CatTipoIncidencia catTipoIncidencia) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;
}
