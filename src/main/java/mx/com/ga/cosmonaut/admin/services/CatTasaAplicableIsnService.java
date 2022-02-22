package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatTasaAplicableIsn;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

import java.util.List;
import java.util.Set;

public interface CatTasaAplicableIsnService {

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;

    RespuestaGenerica guardar(CatTasaAplicableIsn catTasaAplicableIsn) throws ServiceException;
    
    RespuestaGenerica modificar(CatTasaAplicableIsn catTasaAplicableIsn) throws ServiceException;

    RespuestaGenerica modificarMultiple(List<CatTasaAplicableIsn> valoresTasaAplicableIsn) throws ServiceException;

}
