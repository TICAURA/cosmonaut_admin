package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsBanco;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CsBancoService {

    RespuestaGenerica findAll() throws ServiceException;
    
    RespuestaGenerica findByRazonSocial(String razonSocial) throws ServiceException;
    
    RespuestaGenerica findById(Long id) throws ServiceException;

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;
    
    RespuestaGenerica modificar(CsBanco csBanco) throws ServiceException;
    
    RespuestaGenerica guardar(CsBanco csBanco) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;
}
