package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.PermisoService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.permisos.AgregarQuitarRequest;
import mx.com.ga.cosmonaut.common.dto.administracion.permisos.SubmoduloXPermiso;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.*;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.*;
import mx.com.ga.cosmonaut.common.util.Constantes;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class PermisoServiceImpl implements PermisoService {

    @Inject
    private AdmCatPermisoRepository admCatPermisoRepository;

    @Inject
    private AdmCatSubmoduloRepository admCatSubmoduloRepository;

    @Inject
    private AdmCatModulosRepository admCatModulosRepository;

    @Inject
    private AdmPermisosXsubmoduloRepository admPermisosXsubmoduloRepository;

    @Inject
    private AdmPermisosXsubmoduloXrolRepository admPermisosXsubmoduloXrolRepository;

    @Inject
    private AdmRolesRepository admRolesRepository;

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            respuestaGenerica.setDatos(admCatPermisoRepository.findByEsActivoOrderByDescripcion(activo));
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            respuestaGenerica.setMensaje(Constantes.EXITO);
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" findByEsActivo" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

    @Override
    public RespuestaGenerica findBySubmoduloIdAndEsActivo(Integer id, Boolean activo) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            Optional<AdmCatSubmodulo> submodulo = admCatSubmoduloRepository.findById(id);

            if (submodulo.isPresent()) {
                respuestaGenerica.setDatos(admPermisosXsubmoduloRepository.findBySubmoduloIdAndSubmoduloIdEsActivoAndPermisoIdEsActivo(submodulo.get(), activo, activo));
                respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
                respuestaGenerica.setMensaje(Constantes.EXITO);
            } else {
                respuestaGenerica.setResultado(Constantes.RESULTADO_ERROR);
                respuestaGenerica.setMensaje(Constantes.ERROR_VERSION_NO_EXISTE);
            }
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" findBySubmoduloIdAndEsActivo" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

    @Override
    public RespuestaGenerica agregar(AgregarQuitarRequest request) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            AdmPermisosXsubmoduloXrol admPermisosXsubmoduloXrol = new AdmPermisosXsubmoduloXrol();
            Optional<AdmRoles> rol = admRolesRepository.findById(request.getRolId());
            if (rol.isEmpty()) {
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.ERROR_ROL_NO_EXISTE);
                return respuesta;
            }

            Set<AdmPermisosXsubmodulo> sxps = new HashSet<>();
            for (SubmoduloXPermiso sxp : request.getSubmodulosXpemisos()) {
                Optional<AdmPermisosXsubmodulo> permisosXsubmodulo =
                        admPermisosXsubmoduloRepository.findBySubmoduloIdSubmoduloIdAndPermisoIdPermisoId
                                (sxp.getSubmoduloId(), sxp.getPermisoId());
                if (permisosXsubmodulo.isPresent()) {
                    sxps.add(permisosXsubmodulo.get());
                } else {
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                    respuesta.setMensaje(Constantes.ERROR_SUBMODULOXPERMISO_INEXIST);
                    return respuesta;
                }
            }

            for (AdmPermisosXsubmodulo pxs : sxps) {
                admPermisosXsubmoduloXrol.setRolId(rol.get());
                admPermisosXsubmoduloXrol.setPermisoXsubmoduloId(pxs);
                admPermisosXsubmoduloXrolRepository.save(admPermisosXsubmoduloXrol);
            }

            respuesta.setDatos(rol.get());
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            respuesta.setMensaje(Constantes.EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " agregar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica quitar(AgregarQuitarRequest request) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            Optional<AdmRoles> rol = admRolesRepository.findById(request.getRolId());
            if (rol.isEmpty()) {
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.ERROR_ROL_NO_EXISTE);
                return respuesta;
            }

            Set<AdmPermisosXsubmodulo> sxps = new HashSet<>();
            for (SubmoduloXPermiso sxp : request.getSubmodulosXpemisos()) {
                Optional<AdmPermisosXsubmodulo> permisosXsubmodulo =
                        admPermisosXsubmoduloRepository.findBySubmoduloIdSubmoduloIdAndPermisoIdPermisoId
                                (sxp.getSubmoduloId(), sxp.getPermisoId());
                if (permisosXsubmodulo.isPresent()) {
                    sxps.add(permisosXsubmodulo.get());
                } else {
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                    respuesta.setMensaje(Constantes.ERROR_SUBMODULOXPERMISO_INEXIST);
                    return respuesta;
                }
            }

            for (AdmPermisosXsubmodulo pxs : sxps) {
                Optional<AdmPermisosXsubmoduloXrol> pxsxr = admPermisosXsubmoduloXrolRepository
                        .findByRolIdAndPermisoXsubmoduloId(rol.get(), pxs);
                pxsxr.ifPresent(admPermisosXsubmoduloXrol
                        -> admPermisosXsubmoduloXrolRepository.delete(admPermisosXsubmoduloXrol));
            }

            respuesta.setDatos(rol.get());
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            respuesta.setMensaje(Constantes.EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " quitar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica findByRol(Integer rolId, Boolean esActivo) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            Optional<AdmRoles> rol = admRolesRepository.findById(rolId);
            if (rol.isPresent()) {
                Set<AdmPermisosXsubmodulo> pxss =
                        admPermisosXsubmoduloXrolRepository.findPermisoXsubmoduloIdByRolId(rol.get());

                Set<SubmoduloXPermiso> respuesta = pxss.stream().map(pxs -> {
                    SubmoduloXPermiso data = new SubmoduloXPermiso();
                    data.setPermisoId(pxs.getPermisoId().getPermisoId());
                    data.setSubmoduloId(pxs.getSubmoduloId().getSubmoduloId());
                    return data;
                }).collect(Collectors.toSet());

                respuestaGenerica.setDatos(respuesta);
                respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
                respuestaGenerica.setMensaje(Constantes.EXITO);
            } else {
                respuestaGenerica.setResultado(Constantes.RESULTADO_ERROR);
                respuestaGenerica.setMensaje(Constantes.ERROR_ROLPERMISO_NO_EXISTE);
            }
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" findByRol" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

}
