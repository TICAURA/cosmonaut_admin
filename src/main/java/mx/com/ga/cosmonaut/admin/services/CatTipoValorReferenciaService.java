package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatTipoValorReferencia;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CatTipoValorReferenciaService {

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;

    RespuestaGenerica guardar(CatTipoValorReferencia catTipoValorReferencia) throws ServiceException;

    RespuestaGenerica modificar(CatTipoValorReferencia catTipoValorReferencia) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;

}
