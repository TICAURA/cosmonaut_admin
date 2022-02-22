package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.ubicacion.CatNacionalidad;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CatNacionalidadService {

    RespuestaGenerica findAll() throws ServiceException;

    RespuestaGenerica findById(Long idNacionalidad) throws ServiceException;

    RespuestaGenerica findByEsActivo(Boolean estatus) throws ServiceException;

    RespuestaGenerica guardar(CatNacionalidad catNacionalidad) throws ServiceException;

    RespuestaGenerica modificar(CatNacionalidad catNacionalidad) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;

}
