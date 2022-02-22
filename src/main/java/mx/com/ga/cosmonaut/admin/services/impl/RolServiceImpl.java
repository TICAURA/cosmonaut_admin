package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.RolService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.roles.GuardarRequest;
import mx.com.ga.cosmonaut.common.dto.administracion.roles.ModificarRequest;
import mx.com.ga.cosmonaut.common.dto.administracion.roles.ObtenerRolesPorCteResponse;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmRolXcliente;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmRoles;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmUsuarios;
import mx.com.ga.cosmonaut.common.entity.cliente.NclCentrocCliente;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmRolXclienteRepository;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmRolesRepository;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmUsuariosRepository;
import mx.com.ga.cosmonaut.common.repository.cliente.NclCentrocClienteRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;
import mx.com.ga.cosmonaut.common.util.ObjetoMapper;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class RolServiceImpl implements RolService {

    @Inject
    private AdmRolesRepository admRolesRepository;

    @Inject
    private AdmUsuariosRepository admUsuariosRepository;

    @Inject
    private AdmRolXclienteRepository admRolXclienteRepository;

    @Inject
    private NclCentrocClienteRepository nclCentrocClienteRepository;

    @Override
    public RespuestaGenerica guardar(GuardarRequest request) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();

            Optional<NclCentrocCliente> centrocCliente = nclCentrocClienteRepository
                    .findById(request.getCentrocClienteId());
            if (centrocCliente.isPresent()) {
                AdmRoles admRoles = ObjetoMapper.map(request, AdmRoles.class);

                if (validaDuplicado(request.getCentrocClienteId(),admRoles.getNombreRol())) {
                    admRoles.setEsActivo(Constantes.ESTATUS_ACTIVO);
                    admRoles = admRolesRepository.save(admRoles);

                    AdmRolXcliente admRolXcliente = new AdmRolXcliente();
                    admRolXcliente.setRoldId(admRoles);
                    admRolXcliente.setCentrocClienteId(centrocCliente.get());
                    admRolXclienteRepository.save(admRolXcliente);

                    respuesta.setDatos(admRoles);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                    respuesta.setMensaje(Constantes.EXITO);
                }else {
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                    respuesta.setMensaje("El rol ya existe");
                }
            } else {
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.ERROR_CLIENTE_NO_EXISTE);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " guardar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica modificar(ModificarRequest request) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();

            Optional<AdmRoles> rol = admRolesRepository.findById(request.getRolId());
            if (rol.isPresent()) {
                if (rol.get().getNombreRol().toUpperCase().trim().equals(request.getNombreRol().toUpperCase().trim())) {
                    respuesta.setDatos(admRolesRepository.update(ObjetoMapper.map(request, rol.get())));
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                    respuesta.setMensaje(Constantes.EXITO);
                } else {
                     Integer idCentro = admRolesRepository.findByCentroCostoClienteByIdRol(request.getRolId());
                    if (validaDuplicado(idCentro,request.getNombreRol())) {
                            respuesta.setDatos(admRolesRepository.update(ObjetoMapper.map(request, rol.get())));
                            respuesta.setResultado(Constantes.RESULTADO_EXITO);
                            respuesta.setMensaje(Constantes.EXITO);
                        }else{
                            respuesta.setResultado(Constantes.RESULTADO_ERROR);
                            respuesta.setMensaje("El rol ya existe");
                        }
                    }
                }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private boolean validaDuplicado(Integer id, String desc) throws ServiceException {
        try {
            return admRolesRepository.findByRolCentro(id, desc.toUpperCase().trim()).isEmpty();
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaDuplicado " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica eliminar(Integer rolId) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();

            Optional<AdmRoles> rol = admRolesRepository.findById(rolId);
            if (rol.isPresent()) {
                // VALIDAR SI EL ROL ESTA ASIGNADO PARA NO PERMITIR BORRAR
                if (admUsuariosRepository.countByRolIdRolId(rolId) > 0) {
                    return new RespuestaGenerica(null, Constantes.RESULTADO_ERROR, Constantes.ERROR_ROL_ASIGNADO);
                }

                admRolesRepository.update(rolId, Utilidades.obtenerFechaSystema(), Constantes.ESTATUS_INACTIVO);
                respuesta.setDatos(null);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
                respuesta.setMensaje(Constantes.EXITO);
            } else {
                respuesta = new RespuestaGenerica(null, Constantes.RESULTADO_ERROR, Constantes.ID_NULO);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " eliminar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica obtenerId(Integer rolId) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(admRolesRepository.findById(rolId));
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            respuesta.setMensaje(Constantes.EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " obtenerId " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            respuestaGenerica.setDatos(admRolesRepository.findByEsActivoOrderByNombreRol(activo));
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            respuestaGenerica.setMensaje(Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

    @Override
    public RespuestaGenerica listByRolIdRolIdAndCentrocClienteId(Integer id,Integer idVersion, Integer idRol) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
             Optional<NclCentrocCliente> centrocClienteOp = nclCentrocClienteRepository.findById(id);
            // SI ES CTE CUENTA TODOS LOS EMPLEADOS PROPIOS Y DE SUS SUBEMPRESAS DE LOS
            // ROLES PROPIOS
            if (centrocClienteOp.isPresent() && centrocClienteOp.get().getCentroCostosCentrocClienteId() == null) {
                    if (idVersion == Constantes.VERSION_ADMON) {
                        respuestaGenerica.setDatos(admUsuariosRepository
                                .cantidadRolesVersionCosmonautUsers(
                                        idRol));
                        respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
                        respuestaGenerica.setMensaje(Constantes.EXITO);
                    } else {
                        respuestaGenerica.setDatos(admUsuariosRepository
                                .countByRolIdRolIdAndCentrocClientesCentrocClienteIdOrCentrocClientesCentroCostosCentrocClienteIdUsers(
                                        idRol, id));
                        respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
                        respuestaGenerica.setMensaje(Constantes.EXITO);
                    }
                // SI ES EMPRESA CUENTA LOS EMPLEADOS PROPIOS DE LOS ROLES PROPIOS
            } else if (centrocClienteOp.isPresent()
                    && centrocClienteOp.get().getCentroCostosCentrocClienteId() != null) {
                respuestaGenerica.setDatos(admUsuariosRepository
                            .listByRolIdRolIdAndCentrocClientesCentrocClienteId(id,idRol));
                respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
                respuestaGenerica.setMensaje(Constantes.EXITO);
            }


          //  respuestaGenerica.setDatos(admUsuariosRepository.listByRolIdRolIdAndCentrocClientesCentrocClienteId(id, idRol));

        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " Listar Rol por centro" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

    @Override
    public RespuestaGenerica findByCentrocClientesCentrocClienteIdAndEsActivo(Integer id, Integer idVersion,
            Boolean activo) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            List<AdmRoles> roles = admRolesRepository.findByCentrocClientesCentrocClienteIdAndEsActivo(id, activo);

            Optional<AdmRoles> rolEspecial;
            if (idVersion == Constantes.VERSION_ADMON) {
                rolEspecial = admRolesRepository.findById(Constantes.ROL_CONTACTO);
                rolEspecial.ifPresent(roles::add);
                rolEspecial = admRolesRepository.findById(Constantes.ROL_ADMON);
                rolEspecial.ifPresent(roles::add);
            } else {
                rolEspecial = admRolesRepository.findById(Constantes.ROL_EMP);
                rolEspecial.ifPresent(roles::add);
            }

            List<ObtenerRolesPorCteResponse> datos = new ArrayList<>();
            Optional<NclCentrocCliente> centrocClienteOp = nclCentrocClienteRepository.findById(id);
            // SI ES CTE CUENTA TODOS LOS EMPLEADOS PROPIOS Y DE SUS SUBEMPRESAS DE LOS
            // ROLES PROPIOS
            if (centrocClienteOp.isPresent() && centrocClienteOp.get().getCentroCostosCentrocClienteId() == null) {
                datos = roles.stream().map(r -> {
                    ObtenerRolesPorCteResponse data = ObjetoMapper.map(r, ObtenerRolesPorCteResponse.class);
                    if (idVersion == Constantes.VERSION_ADMON) {
                        data.setNoUsuarios(admUsuariosRepository
                                .cantidadRolesVersionCosmonaut(
                                        data.getRolId()));
                    } else {
                        data.setNoUsuarios(admUsuariosRepository
                                .countByRolIdRolIdAndCentrocClientesCentrocClienteIdOrCentrocClientesCentroCostosCentrocClienteId(
                                        data.getRolId(), id));
                    }
                    return data;
                }).collect(Collectors.toList());
                // SI ES EMPRESA CUENTA LOS EMPLEADOS PROPIOS DE LOS ROLES PROPIOS
            } else if (centrocClienteOp.isPresent()
                    && centrocClienteOp.get().getCentroCostosCentrocClienteId() != null) {
                datos = roles.stream().map(r -> {
                    ObtenerRolesPorCteResponse data = ObjetoMapper.map(r, ObtenerRolesPorCteResponse.class);
                    data.setNoUsuarios(admUsuariosRepository
                            .countByRolIdRolIdAndCentrocClientesCentrocClienteId(data.getRolId(), id));
                    return data;
                }).collect(Collectors.toList());
            }

            respuestaGenerica.setDatos(datos);
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            respuestaGenerica.setMensaje(Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

}
