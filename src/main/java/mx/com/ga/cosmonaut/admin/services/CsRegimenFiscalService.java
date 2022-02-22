package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsRegimenFiscal;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CsRegimenFiscalService {

    RespuestaGenerica findAll() throws ServiceException;
    
    RespuestaGenerica findById(String id) throws ServiceException;

    RespuestaGenerica findEsActivo(Boolean activo) throws ServiceException;
    
    RespuestaGenerica modificar(CsRegimenFiscal csRegimenFiscal) throws ServiceException;
    
    RespuestaGenerica guardar(CsRegimenFiscal csRegimenFiscal) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;


}
