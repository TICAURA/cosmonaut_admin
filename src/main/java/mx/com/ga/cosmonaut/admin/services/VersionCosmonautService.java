package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.preferencias.PreferenciasColores;
import mx.com.ga.cosmonaut.common.dto.administracion.version.AsignarQuitarRequest;
import mx.com.ga.cosmonaut.common.dto.administracion.version.ModificarRequest;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface VersionCosmonautService {

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;

    RespuestaGenerica findSubmodulosByVersionIdAndEsActivo(Integer id, Boolean activo) throws ServiceException;

    RespuestaGenerica asignar(AsignarQuitarRequest request, PreferenciasColores preferencias) throws ServiceException;

    RespuestaGenerica modificar(ModificarRequest request,PreferenciasColores preferencias) throws ServiceException;

    RespuestaGenerica findVersionByClienteId(Integer clienteId) throws ServiceException;

    RespuestaGenerica findAll() throws ServiceException;

}
