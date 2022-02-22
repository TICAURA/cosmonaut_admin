package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTipoRegimenContratacion;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CsTipoRegimenContratacionService {

    RespuestaGenerica findAll() throws ServiceException;
    
    
    RespuestaGenerica findById(String id) throws ServiceException;

    RespuestaGenerica findByEsActivo(Boolean activo) throws  ServiceException;
    
    RespuestaGenerica modificar(CsTipoRegimenContratacion csTipoRegimenContratacion) throws ServiceException;
    
    RespuestaGenerica guardar(CsTipoRegimenContratacion csTipoRegimenContratacion) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;
}
