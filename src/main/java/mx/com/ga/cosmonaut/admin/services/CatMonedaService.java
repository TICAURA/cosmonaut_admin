package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatMoneda;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CatMonedaService {

    RespuestaGenerica findAll() throws ServiceException;
    
    RespuestaGenerica findByMonedaId(Long monedaId) throws ServiceException;

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;

    RespuestaGenerica guardar(CatMoneda catMoneda) throws ServiceException;

    RespuestaGenerica modificar(CatMoneda catMoneda) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;

}
