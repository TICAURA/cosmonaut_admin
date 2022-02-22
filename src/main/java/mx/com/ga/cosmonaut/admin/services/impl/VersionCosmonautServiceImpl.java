package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.VersionCosmonautService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.preferencias.PreferenciasColores;
import mx.com.ga.cosmonaut.common.dto.administracion.usuarios.AdmVersionCosmonautAndColores;
import mx.com.ga.cosmonaut.common.dto.administracion.version.AsignarQuitarRequest;
import mx.com.ga.cosmonaut.common.dto.administracion.version.ModificarRequest;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmPreferenciasCliente;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmVersionCosmonaut;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmVersionCosmonautXcliente;
import mx.com.ga.cosmonaut.common.entity.cliente.NclCentrocCliente;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.*;
import mx.com.ga.cosmonaut.common.repository.cliente.NclCentrocClienteRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;
import mx.com.ga.cosmonaut.common.util.ObjetoMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class VersionCosmonautServiceImpl implements VersionCosmonautService {

    @Inject
    private AdmVersionCosmonautRepository admVersionCosmonautRepository;

    @Inject
    private AdmVersionCosmonautXsubmoduloRepository admVersionCosmonautXsubmoduloRepository;

    @Inject
    private AdmVersionCosmonautXclienteRepository admVersionCosmonautXclienteRepository;

    @Inject
    private NclCentrocClienteRepository nclCentrocClienteRepository;

    @Inject
    private AdmVersionSubmoduloPermisoRepository admVersionSubmoduloPermisoRepository;
    @Inject
    private AdmPreferenciasClienteRepository admPreferenciasRepository;

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            respuestaGenerica.setDatos(admVersionCosmonautRepository.findByEsActivoOrderByNombreVersion(activo));
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            respuestaGenerica.setMensaje(Constantes.EXITO);
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" findByEsActivo" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

    @Override
    public RespuestaGenerica findSubmodulosByVersionIdAndEsActivo(Integer id, Boolean activo) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            Optional<AdmVersionCosmonaut> versionCosmonaut = admVersionCosmonautRepository.findById(id);
            if (versionCosmonaut.isPresent()) {
                respuestaGenerica.setDatos(admVersionCosmonautXsubmoduloRepository
                        .findByVersionCosmonautIdAndVersionCosmonautIdEsActivo(versionCosmonaut.get(), activo));
                respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
                respuestaGenerica.setMensaje(Constantes.EXITO);
            } else {
                respuestaGenerica.setResultado(Constantes.RESULTADO_ERROR);
                respuestaGenerica.setMensaje(Constantes.ERROR_USUARIO_NO_EXISTE);
            }
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" findSubmodulosByVersionIdAndEsActivo" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

    @Override
    public RespuestaGenerica asignar(AsignarQuitarRequest request, PreferenciasColores preferencias) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();

            Optional<AdmVersionCosmonaut> version = admVersionCosmonautRepository
                    .findById(request.getVersionId());
            Optional<NclCentrocCliente> centrocCliente =
                    nclCentrocClienteRepository.findById(request.getCentrocClienteId());
            if (version.isPresent() && centrocCliente.isPresent()) {
                AdmVersionCosmonautXcliente admUsuarioXcliente = new AdmVersionCosmonautXcliente();
                admUsuarioXcliente.setVersionCosmonautId(version.get());
                admUsuarioXcliente.setCentrocClienteId(centrocCliente.get());


                if(preferencias.getColorfondo() != null){
                    AdmPreferenciasCliente enviarPreferencias = new AdmPreferenciasCliente();
                    enviarPreferencias.setColorFondo(preferencias.getColorfondo());
                    enviarPreferencias.setColorMenu(preferencias.getColormenu());
                    enviarPreferencias.setClienteId(Long.parseLong(String.valueOf(request.getCentrocClienteId())));
                    enviarPreferencias.setMostrarLogoSistema(preferencias.isMostrarlogosistema());
                    admPreferenciasRepository.save(enviarPreferencias);
                }

                respuesta.setDatos(admVersionCosmonautXclienteRepository.save(admUsuarioXcliente));
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
                respuesta.setMensaje(Constantes.EXITO);
            } else {
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.ERROR_ROLPERMISO_NO_EXISTE);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " asignar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica modificar(ModificarRequest request,PreferenciasColores preferencias) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();

            Optional<AdmVersionCosmonautXcliente> versionCosmonautXcliente = admVersionCosmonautXclienteRepository
                    .findById(request.getVersionCosmonautXclienteId());
            if (versionCosmonautXcliente.isEmpty()) {
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.ERROR_VERSIONXCLIENTE_NO_EXISTE);
                return respuesta;
            }
            AdmVersionCosmonautXcliente versionCosmonautXclienteGet = versionCosmonautXcliente.get();

            if (request.getVersionId() != null) {
                Optional<AdmVersionCosmonaut> version = admVersionCosmonautRepository
                        .findById(request.getVersionId());

                if (version.isEmpty()) {
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                    respuesta.setMensaje(Constantes.ERROR_VERSION_NO_EXISTE);
                    return respuesta;
                }

                versionCosmonautXclienteGet.setVersionCosmonautId(version.get());
            }

            if (request.getCentrocClienteId() != null) {
                Optional<NclCentrocCliente> centrocCliente =
                        nclCentrocClienteRepository.findById(request.getCentrocClienteId());

                if (centrocCliente.isEmpty()) {
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                    respuesta.setMensaje(Constantes.ERROR_CLIENTE_NO_EXISTE);
                    return respuesta;
                }

                versionCosmonautXclienteGet.setCentrocClienteId(centrocCliente.get());
            }


            if(preferencias.getColorfondo() != null){
                AdmPreferenciasCliente enviarPreferencias = new AdmPreferenciasCliente();
                enviarPreferencias.setColorFondo(preferencias.getColorfondo());
                enviarPreferencias.setColorMenu(preferencias.getColormenu());
                enviarPreferencias.setClienteId(Long.parseLong(String.valueOf(request.getCentrocClienteId())));
                enviarPreferencias.setMostrarLogoSistema(preferencias.isMostrarlogosistema());
                enviarPreferencias.setId(preferencias.getPreferenciaId());
                if(preferencias.getPreferenciaId() == null){
                    admPreferenciasRepository.save(enviarPreferencias);
                }else{
                    enviarPreferencias.setId(preferencias.getPreferenciaId());
                    admPreferenciasRepository.update(enviarPreferencias);
                }
            }


            respuesta.setDatos(admVersionCosmonautXclienteRepository.update(versionCosmonautXclienteGet));
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            respuesta.setMensaje(Constantes.EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica findVersionByClienteId(Integer clienteId) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();

            Optional<NclCentrocCliente> centrocCliente = nclCentrocClienteRepository.findById(clienteId);
            if (centrocCliente.isPresent()) {

                AdmVersionCosmonautAndColores admColores = ObjetoMapper.map(admVersionCosmonautXclienteRepository.findByCentrocClienteId(centrocCliente.get()),AdmVersionCosmonautAndColores.class);
                try{
                    AdmPreferenciasCliente admColor =  admPreferenciasRepository.findByClienteId(Long.parseLong(String.valueOf(admColores.getCentrocClienteId().getCentrocClienteId())));
                    PreferenciasColores colores = new PreferenciasColores();
                    colores.setColormenu(admColor.getColorMenu());
                    colores.setColorfondo(admColor.getColorFondo());
                    colores.setPreferenciaId(admColor.getId());
                    colores.setMostrarlogosistema(admColor.isMostrarLogoSistema());
                    admColores.setColor(colores);
                }catch (Exception e){

                }
                respuesta.setDatos(admColores);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
                respuesta.setMensaje(Constantes.EXITO);
            } else {
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.ERROR_ROLPERMISO_NO_EXISTE);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findValorReferenciaByEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica findAll() throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            respuestaGenerica.setDatos(admVersionSubmoduloPermisoRepository.findAll());
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            respuestaGenerica.setMensaje(Constantes.EXITO);
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" findAll" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

}
