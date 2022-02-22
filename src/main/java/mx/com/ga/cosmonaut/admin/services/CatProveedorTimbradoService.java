package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatProveedorTimbrado;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CatProveedorTimbradoService {

    RespuestaGenerica listar() throws ServiceException;

    RespuestaGenerica obtenerId(Integer idProveedorDispersion) throws ServiceException;

    RespuestaGenerica listarActivos(Boolean activo) throws ServiceException;

    RespuestaGenerica modificar(CatProveedorTimbrado proveedorTimbrado) throws ServiceException;

    RespuestaGenerica guardar(CatProveedorTimbrado proveedorTimbrado) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;

}
