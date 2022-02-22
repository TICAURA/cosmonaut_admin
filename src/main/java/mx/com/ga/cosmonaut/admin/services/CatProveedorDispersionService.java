package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatProveedorDispersion;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CatProveedorDispersionService {

    RespuestaGenerica listar() throws ServiceException;

    RespuestaGenerica obtenerId(Integer idProveedorDispersion) throws ServiceException;

    RespuestaGenerica listarActivos(Boolean activo) throws ServiceException;

    RespuestaGenerica modificar(CatProveedorDispersion proveedorDispersion) throws ServiceException;

    RespuestaGenerica guardar(CatProveedorDispersion proveedorDispersion) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;

}
