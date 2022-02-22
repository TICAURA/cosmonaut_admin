package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTipoContrato;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CsTipoContratoService {

    RespuestaGenerica findAll() throws ServiceException;

    RespuestaGenerica findById(String id) throws ServiceException;

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;
    
    RespuestaGenerica modificar(CsTipoContrato csTipoContrato) throws ServiceException;
    
    RespuestaGenerica guardar(CsTipoContrato csTipoContrato) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;
}
