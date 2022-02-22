package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmMensajeChatCentrocostos;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface AdministrarMensajeChatService {
    RespuestaGenerica guardar(AdmMensajeChatCentrocostos admMensajeChatCentrocostos) throws ServiceException;

    RespuestaGenerica modificar(AdmMensajeChatCentrocostos admMensajeChatCentrocostos) throws ServiceException;

    RespuestaGenerica obtenerId(Integer mensajeChatCentrocostosId) throws ServiceException;

    RespuestaGenerica listarEmpresa(Integer idEmpresa) throws ServiceException;

    RespuestaGenerica listarEmpresaUsuario(Integer idEmpresa, Integer idUsuario) throws ServiceException;

}
