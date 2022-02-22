package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.usuarios.*;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface UsuariosService {

    RespuestaGenerica guardar(GuardarRequest request) throws ServiceException;

    RespuestaGenerica modificar(ModificarRequest request) throws ServiceException;

    RespuestaGenerica cambiarPwd(CambiarPwdRequest request) throws ServiceException;

    RespuestaGenerica reestablecerPwd(ReestablecerPwdRequest request) throws ServiceException;

    RespuestaGenerica eliminar(Integer usuarioId) throws ServiceException;

    RespuestaGenerica cambiarEstados(CambiarEstadosRequest request) throws ServiceException;

    RespuestaGenerica obtenerId(Integer usuarioId) throws ServiceException;

    RespuestaGenerica obtenerUsername(String username) throws ServiceException;

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;

    RespuestaGenerica filtrar(FiltradoRequest request) throws ServiceException;

    RespuestaGenerica asignar(AsignarQuitarRequest request) throws ServiceException;

    RespuestaGenerica quitar(AsignarQuitarRequest request) throws ServiceException;

    RespuestaGenerica findClientesByUsuarioIdAndEsActivo(Integer usuarioId, Boolean activo) throws ServiceException;

    RespuestaGenerica filtrarPaginable(FiltradoRequest request, Integer numeroRegistros, Integer pagina) throws ServiceException;

}
