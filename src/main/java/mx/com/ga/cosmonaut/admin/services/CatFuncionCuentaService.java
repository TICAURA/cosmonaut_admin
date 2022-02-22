package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatFuncionCuenta;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface CatFuncionCuentaService {

    RespuestaGenerica findAll() throws ServiceException;

    RespuestaGenerica findById(Integer id) throws ServiceException;

    RespuestaGenerica findEsActivo(Boolean activo) throws ServiceException;

    RespuestaGenerica guardar(CatFuncionCuenta catFuncionCuenta) throws ServiceException;

    RespuestaGenerica modificar(CatFuncionCuenta catFuncionCuenta) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;

}
