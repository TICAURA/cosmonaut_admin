package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.ubicacion.CatMunicipio;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CatMunicipioService {
    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;
    
    RespuestaGenerica modificar(CatMunicipio catMunicipio) throws ServiceException;
    
    RespuestaGenerica guardar(CatMunicipio catMunicipio) throws ServiceException;
}
