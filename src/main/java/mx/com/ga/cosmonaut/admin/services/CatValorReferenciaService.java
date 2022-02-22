
package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatValorReferencia;
import mx.com.ga.cosmonaut.common.exception.ServiceException;


public interface CatValorReferenciaService {

    RespuestaGenerica findAll() throws ServiceException;

    RespuestaGenerica findById(Long valorReferenciaId) throws ServiceException;

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;
    
    RespuestaGenerica modificar(CatValorReferencia catValorReferencia) throws ServiceException;
    
    RespuestaGenerica guardar(CatValorReferencia catValorReferencia) throws ServiceException;

    RespuestaGenerica eliminar(CatValorReferencia catValorReferencia) throws ServiceException;
}
