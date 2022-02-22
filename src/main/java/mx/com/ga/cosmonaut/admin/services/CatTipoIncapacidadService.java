package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatTipoIncapacidad;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CatTipoIncapacidadService {

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;
    
    RespuestaGenerica modificar(CatTipoIncapacidad catTipoIncapacidad) throws ServiceException;
    
    RespuestaGenerica guardar(CatTipoIncapacidad catTipoIncapacidad) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;
}
