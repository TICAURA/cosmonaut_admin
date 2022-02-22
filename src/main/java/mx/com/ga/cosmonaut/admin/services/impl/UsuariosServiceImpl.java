package mx.com.ga.cosmonaut.admin.services.impl;

import io.micronaut.context.annotation.Value;
import mx.com.ga.cosmonaut.admin.services.UsuariosService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.permisos.SubmoduloXPermiso;
import mx.com.ga.cosmonaut.common.dto.administracion.preferencias.PreferenciasColores;
import mx.com.ga.cosmonaut.common.dto.administracion.usuarios.*;
import mx.com.ga.cosmonaut.common.dto.mail.MailObject;
import mx.com.ga.cosmonaut.common.dto.mail.MailResult;
import mx.com.ga.cosmonaut.common.dto.mail.SendGridMailConfig;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.*;
import mx.com.ga.cosmonaut.common.entity.cliente.NclCentrocCliente;
import mx.com.ga.cosmonaut.common.entity.colaborador.NcoPersona;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.*;
import mx.com.ga.cosmonaut.common.repository.cliente.NclCentrocClienteRepository;
import mx.com.ga.cosmonaut.common.repository.colaborador.NcoPersonaRepository;
import mx.com.ga.cosmonaut.common.repository.nativo.UsuarioRepository;
import mx.com.ga.cosmonaut.common.service.MailService;
import mx.com.ga.cosmonaut.common.util.*;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class UsuariosServiceImpl implements UsuariosService {

    @Inject
    private AdmUsuariosRepository admUsuariosRepository;

    @Inject
    private UsuarioRepository usuarioRepository;

    @Inject
    private AdmRolesRepository admRolesRepository;

    @Inject
    private AdmHistoricoPasswordRepository admHistoricoPasswordRepository;

    @Inject
    private AdmUsuarioXclienteRepository admUsuarioXclienteRepository;

    @Inject
    private NclCentrocClienteRepository nclCentrocClienteRepository;

    @Inject
    private AdmEstatusUsuariosChatRepository admEstatusUsuariosChatRepository;

    @Inject
    private AdmPermisosXsubmoduloXrolRepository admPermisosXsubmoduloXrolRepository;

    @Inject
    private AdmCatModulosRepository admCatModulosRepository;

    @Inject
    private NcoPersonaRepository ncoPersonaRepository;

    @Inject
    private MailService mailService;

    @Inject
    private MailTemplateUtil mailTemplateUtil;

    @Inject
    private NcoPersonaRepository personaRepository;

    @Value("${mx.com.ga.cosmonaut.sendgrid.soporte-sender}")
    private String sender;

    @Value("${mx.com.ga.cosmonaut.sendgrid.apikey}")
    private String apikey;
    @Inject
    private AdmPreferenciasClienteRepository admPreferenciasClienteRepository;

    @Override
    @Transactional
    public RespuestaGenerica guardar(GuardarRequest request) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();

            if(request.getVersion().equalsIgnoreCase("1") && request.getRolId() != 1){

                boolean existeCorreo = ncoPersonaRepository.existsByEmailCorporativo(request.getEmail());
                if(existeCorreo){
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                    respuesta.setMensaje("El correo electrónico ya está dado de alta en el sistema, favor de probar con otro correo");
                    return respuesta;
                }

            }


            //VAlida usuario repetido
            if (esEmailRepetido(request.getEmail())) {
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje("El correo electrónico ya está dado de alta en el sistema, favor de probar con otro correo");
                return respuesta;
            }

            // Validacion de cada centro de costos
            Set<NclCentrocCliente> centroscClientes = new HashSet<>();
            for (Integer centrocClienteId : request.getCentrocClienteIds()) {



                Optional<NclCentrocCliente> centrocCliente =
                        nclCentrocClienteRepository.findById(centrocClienteId);
                if (centrocCliente.isPresent()) {
                    centroscClientes.add(centrocCliente.get());
                    //Revisar que tipo de centro cliente id es ...


                } else {
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                    respuesta.setMensaje(Constantes.ERROR_CLIENTE_NO_EXISTE);
                    return respuesta;
                }
            }

            // Longitud de contraseña según rol
            Optional<AdmRoles> rol = admRolesRepository.findById(request.getRolId());
            if (rol.isPresent()) {
                String pwd;
                if (rol.get().getRolId() == Constantes.ROL_ADMON) {
                    pwd = PwdUtil.generatePwd(20);
                } else {
                    pwd = PwdUtil.generatePwd(8);
                }

                AdmUsuarios usuario = ObjetoMapper.map(request, AdmUsuarios.class);
                usuario.setPassword(BCrypt.hashpw(pwd, BCrypt.gensalt()));
                usuario.setEsActivo(Constantes.ESTATUS_ACTIVO);
                usuario.setPasswordProvisional(Constantes.ESTATUS_ACTIVO);
                usuario.setFechaUltimoPassword(Utilidades.obtenerFechaSystema());
                usuario.setRolId(rol.get());

                usuario = admUsuariosRepository.save(usuario);

                for (NclCentrocCliente centrocCliente : centroscClientes) {
                    AdmUsuarioXcliente admUsuarioXcliente = new AdmUsuarioXcliente();
                    admUsuarioXcliente.setUsuarioId(usuario);
                    admUsuarioXcliente.setCentrocClienteId(centrocCliente);
                    admUsuarioXcliente.setEsActivo(Constantes.ESTATUS_ACTIVO);

                    admUsuarioXclienteRepository.save(admUsuarioXcliente);
                }

                // Envio de mail
                System.out.println("***** Contrasenias -> " +pwd);
                mailService.setConfig(new SendGridMailConfig(apikey, sender));
                MailResult mailResult = mailService.enviarCorreo(generarPwdMail(usuario, pwd));
                System.out.println("MailResult: " +mailResult);

                // Creado en tabla adm_estatus_usuario_chat
                AdmEstatusUsuariosChat admEstatusUsuariosChat = new AdmEstatusUsuariosChat();
                admEstatusUsuariosChat.setEnLinea(Constantes.ESTATUS_INACTIVO);
                admEstatusUsuariosChat.setUsuarioId(usuario);

                // Si es empleado pasa a ser usuario_rh false
                if (rol.get().getRolId() == Constantes.ROL_EMP) {
                    admEstatusUsuariosChat.setUsuarioRh(Constantes.ESTATUS_INACTIVO);
                } else {
                    // Si no validacion de permiso de chat
                    admEstatusUsuariosChat = validaPermisoChat(rol.get(), admEstatusUsuariosChat);
                }

                admEstatusUsuariosChatRepository.save(admEstatusUsuariosChat);

                respuesta.setDatos(usuario);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
                respuesta.setMensaje(Constantes.EXITO);
            } else {
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.ERROR_ROL_NO_EXISTE);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " guardar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private MailObject generarPwdMail(AdmUsuarios admUsuarios, String pwd) {
        MailObject mailObject = new MailObject();
        mailObject.setTo(new String[]{admUsuarios.getEmail()});
        mailObject.setSubject(Constantes.MAIL_PWD_SUBJECT);

        Map<String, Object> valores = new HashMap<>();
        valores.put("nombre", admUsuarios.getNombre()+" "+admUsuarios.getApellidoPat());
        valores.put("correo", admUsuarios.getEmail());
        valores.put("pwd", pwd);
        mailObject.setHtmlContent(mailTemplateUtil.renderHtml(Constantes.MAIL_PWD_TEMPLATE, valores));

        return mailObject;
    }

    private AdmEstatusUsuariosChat validaPermisoChat(AdmRoles rol, AdmEstatusUsuariosChat admEstatusUsuariosChat) {
        boolean tienePermisoChat = false;

        Set<Integer> pxsIds = admPermisosXsubmoduloXrolRepository.findPermisoXsubmoduloIdByRolId(rol)
                .stream().map(AdmPermisosXsubmodulo::getPermisoXsubmoduloId).collect(Collectors.toSet());
        for (Integer pxsId : pxsIds) {
            AdmCatModulos modulo = admCatModulosRepository
                    .findBySubmodulosPermisosPermisosXsubmodulosPermisoXsubmoduloId(pxsId);
            for (AdmCatSubmodulo submodulo : modulo.getSubmodulos()) {
                if (submodulo.getNombreSubmodulo().equals(Constantes.SUBMODULO_CHAT)) {
                    tienePermisoChat = true;
                }
            }
        }

        if (tienePermisoChat) {
            admEstatusUsuariosChat.setUsuarioRh(Constantes.ESTATUS_ACTIVO);
        } else {
            admEstatusUsuariosChat.setUsuarioRh(Constantes.ESTATUS_INACTIVO);
        }

        return admEstatusUsuariosChat;
    }

    @Override
    public RespuestaGenerica modificar(ModificarRequest request) throws ServiceException {
        try {
            RespuestaGenerica respuesta =  new RespuestaGenerica();

            Optional<AdmUsuarios> usuario = admUsuariosRepository.findById(request.getUsuarioId());

            if(request.getVersion().equalsIgnoreCase("1") && request.getRolId() != 1){

                if(!usuario.get().getEmail().equalsIgnoreCase(request.getEmail())){
                    boolean existeCorreo = ncoPersonaRepository.existsByEmailCorporativo(request.getEmail());
                    if(existeCorreo){
                        respuesta.setResultado(Constantes.RESULTADO_ERROR);
                        respuesta.setMensaje("El correo electrónico ya está dado de alta en el sistema, favor de probar con otro correo");
                        return respuesta;
                    }
                }
            }
            if (usuario.isPresent()) {

                // PARA VALIDAR SI SE NECESITA CAMBIAR MAIL Y QUE NO SEA CORREO REPETIDO
                String correoViejo = usuario.get().getEmail();
                if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(correoViejo)) {
                    if (esEmailRepetido(request.getEmail())) {
                        respuesta.setResultado(Constantes.RESULTADO_ERROR);
                        respuesta.setMensaje(Constantes.REGISTRO_EXISTE);
                        return respuesta;
                    }
                }

                AdmUsuarios usuarioMod = ObjetoMapper.map(request, usuario.get());
                if (request.getRolId() != null) {
                    Optional<AdmRoles> rol = admRolesRepository.findById(request.getRolId());

                    if (rol.isPresent()) {
                        usuarioMod.setRolId(rol.get());
                    } else {
                        respuesta.setResultado(Constantes.RESULTADO_ERROR);
                        respuesta.setMensaje(Constantes.ERROR_ROL_NO_EXISTE);
                        return respuesta;
                    }
                }

                // Validacion de cada centro de costos
                Set<NclCentrocCliente> centroscClientes = new HashSet<>();
                for (Integer centrocClienteId : request.getCentrocClienteIds()) {

                    Optional<NclCentrocCliente> centrocCliente =
                            nclCentrocClienteRepository.findById(centrocClienteId);
                    if (centrocCliente.isPresent()) {
                        centroscClientes.add(centrocCliente.get());
                    } else {
                        respuesta.setResultado(Constantes.RESULTADO_ERROR);
                        respuesta.setMensaje(Constantes.ERROR_CLIENTE_NO_EXISTE);
                        return respuesta;
                    }
                }


                if(request.getRolId() == 1){ //Es contacto inicial
                  for(Integer mm : request.getCentrocClienteIds()){
                      Optional<NcoPersona> optPersona = ncoPersonaRepository.findByEmailCorporativoAndCentrocClienteIdCentrocClienteId(request.getEmail(),mm);
                      if(optPersona.isPresent()){
                          NcoPersona persona = optPersona.get();
                          if(persona.getTipoPersonaId().getTipoPersonaId() == 2){
                              persona.setNombre(request.getNombre());
                              persona.setApellidoPaterno(request.getApellidoPat());
                              persona.setApellidoMaterno(request.getApellidoMat());
                              persona.setEmailCorporativo(request.getEmail());
                              NclCentrocCliente cliente = new NclCentrocCliente();
                              cliente.setCentrocClienteId(mm);
                              persona.setCentrocClienteId(cliente);
                              ncoPersonaRepository.update(persona);
                          }
                      }
                  }

                }

                usuarioMod = admUsuariosRepository.update(usuarioMod);

                // SI HUBO UN CAMBIO EN EL EMAIl SE REESTABLECE CONTRASENIA
                if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(correoViejo)) {
                    ReestablecerPwdRequest reestablecer = new ReestablecerPwdRequest();
                    reestablecer.setUsername(request.getEmail());
                    reestablecerPwd(reestablecer);
                }

                // Todos los registros se inactivan temporalmente
                admUsuarioXclienteRepository.inactivarTodosByUsuarioId(usuarioMod.getUsuarioId(), Utilidades.obtenerFechaSystema());
                // Si ya esta relacionado y activo no pasa nada se queda tal cual
                for (NclCentrocCliente centrocCliente : centroscClientes) {
                    Optional<AdmUsuarioXcliente> admUsuarioXclienteOptional = admUsuarioXclienteRepository
                            .findByCentrocClienteIdAndUsuarioId(centrocCliente, usuarioMod);
                    if (admUsuarioXclienteOptional.isPresent()) {
                        // Se setea en activo si ya estaba relacionado pero no activo
                        if (!admUsuarioXclienteOptional.get().isEsActivo()) {
                            admUsuarioXclienteRepository.update(admUsuarioXclienteOptional.get().getUsuarioXclienteId(),
                                    null, Constantes.ESTATUS_ACTIVO);
                        }
                    // Se da de alta si no esta relacioado de ninguna forma
                    } else {
                        AdmUsuarioXcliente admUsuarioXcliente = new AdmUsuarioXcliente();
                        admUsuarioXcliente.setUsuarioId(usuarioMod);
                        admUsuarioXcliente.setCentrocClienteId(centrocCliente);
                        admUsuarioXcliente.setEsActivo(Constantes.ESTATUS_ACTIVO);

                        admUsuarioXclienteRepository.save(admUsuarioXcliente);
                    }
                }

                respuesta.setDatos(usuarioMod);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
                respuesta.setMensaje(Constantes.EXITO);
            } else {
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
                respuesta.setMensaje(Constantes.ERROR_USUARIO_NO_EXISTE);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" modificar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private boolean esEmailRepetido(String email) {
        return admUsuariosRepository.findByEmail(email).isPresent();
    }

    @Override
    public RespuestaGenerica cambiarPwd(CambiarPwdRequest request) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();

            if (request.getOldPwd().equals(request.getNewPwd())) {
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.ERROR_PWD_DUPLICADA);
                return respuesta;
            }

            Optional<AdmUsuarios> usuario = admUsuariosRepository.findById(request.getUsuarioId());
            if (usuario.isPresent()) {
                AdmUsuarios usuarioGet = usuario.get();

                //Agregar logica de verificar rol de admon para ponerle un minimo a la contraseña

                for (AdmHistoricoPassword historico :
                        admHistoricoPasswordRepository.findByUsuarioIdOrderByFechaAlta(usuarioGet)) {
                    if (BCrypt.checkpw(request.getNewPwd(), historico.getPassword())) {
                        respuesta.setResultado(Constantes.RESULTADO_ERROR);
                        respuesta.setMensaje(Constantes.ERROR_PWD_DUPLICADA);
                        return respuesta;
                    }
                }

                if (BCrypt.checkpw(request.getOldPwd(), usuarioGet.getPassword())) {
                    admUsuariosRepository.update(request.getUsuarioId(),
                            BCrypt.hashpw(request.getNewPwd(), BCrypt.gensalt()), Constantes.ESTATUS_INACTIVO,
                            Utilidades.obtenerFechaSystema());

                    AdmHistoricoPassword admHistoricoPassword = new AdmHistoricoPassword();
                    admHistoricoPassword.setUsuarioId(usuarioGet);
                    admHistoricoPassword.setPassword(usuarioGet.getPassword());
                    admHistoricoPasswordRepository.save(admHistoricoPassword);

                    respuesta.setDatos(null);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                    respuesta.setMensaje(Constantes.EXITO);
                } else {
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                    respuesta.setMensaje(Constantes.ERROR_OLDPWD_NO_COINCIDE);
                }
            } else {
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.ERROR_USUARIO_NO_EXISTE);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" modificar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica reestablecerPwd(ReestablecerPwdRequest request) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            Optional<AdmUsuarios> usuario = admUsuariosRepository.findByEmail(request.getUsername());
            if (usuario.isPresent()) {
                AdmUsuarios usuarioGet = usuario.get();

                //Agregar logica de verificar rol de admon para ponerle un minimo a la contraseña
                Optional<AdmRoles> rol = admRolesRepository.findById(usuarioGet.getRolId().getRolId());
                String newPwd;
                if (rol.isPresent()) {
                    if (rol.get().getRolId() == Constantes.ROL_ADMON) {
                        newPwd = PwdUtil.generatePwd(20);
                    } else {
                        newPwd = PwdUtil.generatePwd(8);
                    }
                } else {
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                    respuesta.setMensaje(Constantes.ERROR);
                    return respuesta;
                }

                //Agregar logica de envio de email con pwd al usuario
                System.out.println("***** Contrasenias -> " +newPwd);
                mailService.setConfig(new SendGridMailConfig(apikey, sender));
                MailResult mailResult = mailService.enviarCorreo(generarReestablecerMail(usuarioGet, newPwd));
                System.out.println("MailResult: " +mailResult);

                admUsuariosRepository.update(usuarioGet.getUsuarioId(),
                        BCrypt.hashpw(newPwd, BCrypt.gensalt()),
                        Constantes.ESTATUS_ACTIVO, Utilidades.obtenerFechaSystema());

                AdmHistoricoPassword admHistoricoPassword = new AdmHistoricoPassword();
                admHistoricoPassword.setUsuarioId(usuarioGet);
                admHistoricoPassword.setPassword(usuarioGet.getPassword());
                admHistoricoPasswordRepository.save(admHistoricoPassword);

                respuesta.setDatos(new ReestablecerPwdResponse(BCrypt.hashpw(newPwd, BCrypt.gensalt())));
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
                respuesta.setMensaje(Constantes.EXITO);
            } else {
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.ERROR_USUARIO_NO_EXISTE);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" modificar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private MailObject generarReestablecerMail(AdmUsuarios admUsuarios, String pwd) {
        MailObject mailObject = new MailObject();
        mailObject.setTo(new String[]{admUsuarios.getEmail()});
        mailObject.setSubject(Constantes.MAIL_REESTABLECIMIENTO_SUBJECT);

        Map<String, Object> valores = new HashMap<>();
        valores.put("nombre", admUsuarios.getNombre()+" "+admUsuarios.getApellidoPat());
        valores.put("pwd", pwd);
        mailObject.setHtmlContent(mailTemplateUtil.renderHtml(Constantes.MAIL_REESTABLECIMIENTO_TEMPLATE, valores));

        return mailObject;
    }

    @Override
    public RespuestaGenerica eliminar(Integer usuarioId) throws ServiceException {
        try {
            RespuestaGenerica respuesta =  new RespuestaGenerica();
            if (usuarioId != null){
                admUsuariosRepository.update(usuarioId, Utilidades.obtenerFechaSystema(), Constantes.ESTATUS_INACTIVO);
                respuesta.setDatos(null);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
                respuesta.setMensaje(Constantes.EXITO);
            }else{
                respuesta = new RespuestaGenerica(null, Constantes.RESULTADO_ERROR, Constantes.ID_NULO);
            }
            return respuesta;
        } catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" modificar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica cambiarEstados(CambiarEstadosRequest request) throws ServiceException {
        try {
            RespuestaGenerica respuesta =  new RespuestaGenerica();

            request.getIds().forEach(id ->
                    admUsuariosRepository.update(id, Utilidades.obtenerFechaSystema(), request.getEsActivo()));

            respuesta.setDatos(null);
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            respuesta.setMensaje(Constantes.EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" cambiarEstados " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica obtenerId(Integer usuarioId) throws ServiceException {
        try{
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(admUsuariosRepository.findById(usuarioId));
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            respuesta.setMensaje(Constantes.EXITO);
            return respuesta;
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" obtenerId " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica obtenerUsername(String username) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            Optional<AdmUsuarios> usuario = admUsuariosRepository.findByEmail(username);
            if (usuario.isPresent()) {
                ObtenerUsernameResponse response = new ObtenerUsernameResponse();
                response.setUsuario(usuario.get());

                List<NclCentrocCliente> clientes = admUsuarioXclienteRepository
                        .findCentrocClienteIdByUsuarioIdAndEsActivo(usuario.get(), Constantes.ESTATUS_ACTIVO);
                if (!clientes.isEmpty()) {
                    response.setClientes(clientes);
                }

                List<NcoPersona> personas = ncoPersonaRepository.findByEmailCorporativo(username);
                if(!personas.isEmpty()) {
                    response.setPersonas(personas);
                }

                Optional<AdmRoles> rol = admRolesRepository.findById(usuario.get().getRolId().getRolId());
                if (rol.isPresent()) {
                    Set<AdmPermisosXsubmodulo> pxss =
                            admPermisosXsubmoduloXrolRepository.findPermisoXsubmoduloIdByRolId(rol.get());

                    Set<SubmoduloXPermiso> submodulosXPermisos = pxss.stream().map(pxs -> {
                        SubmoduloXPermiso data = new SubmoduloXPermiso();
                        data.setPermisoId(pxs.getPermisoId().getPermisoId());
                        data.setSubmoduloId(pxs.getSubmoduloId().getSubmoduloId());
                        return data;
                    }).collect(Collectors.toSet());

                    response.setSubmodulosXpermisos(submodulosXPermisos);
                }


                List<PreferenciasColores> preferenciasColores = new ArrayList<>();
                clientes.stream().forEach(s -> {
                    try{
                        AdmPreferenciasCliente admColor =  admPreferenciasClienteRepository.findByClienteId(Long.parseLong(String.valueOf(s.getCentrocClienteId())));
                        PreferenciasColores color = new PreferenciasColores();
                        color.setColorfondo(admColor.getColorFondo());
                        color.setColormenu(admColor.getColorMenu());
                        color.setPreferenciaId(admColor.getId());
                        color.setClienteId(admColor.getClienteId());
                        preferenciasColores.add(color);
                    }catch (Exception e){

                    }

                });
                response.setColoresDefecto(preferenciasColores);
                respuesta.setDatos(response);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
                respuesta.setMensaje(Constantes.EXITO);
            } else {
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.ERROR_USUARIO_NO_EXISTE);
            }
            return respuesta;
        } catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" obtenerUsername " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            respuestaGenerica.setDatos(admUsuariosRepository.findByEsActivoOrderByEmail(activo));
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            respuestaGenerica.setMensaje(Constantes.EXITO);
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" findByEsActivo" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

    @Override
    public RespuestaGenerica filtrar(FiltradoRequest request) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            List<AdmUsuarios> usuarios = usuarioRepository.filtrar(request);

            for (AdmUsuarios usuario : usuarios) {
                admRolesRepository.findById(usuario.getRolId().getRolId())
                        .ifPresent(usuario::setRolId);
                usuario.setCentrocClientes(admUsuarioXclienteRepository
                        .findCentrocClienteIdByUsuarioIdAndEsActivo(usuario, Constantes.ESTATUS_ACTIVO));
            }

            respuestaGenerica.setDatos(usuarios);
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            respuestaGenerica.setMensaje(Constantes.EXITO);
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" findByEsActivo" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

    @Override
    public RespuestaGenerica asignar(AsignarQuitarRequest request) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            Optional<AdmUsuarios> usuario = admUsuariosRepository.findById(request.getUsuarioId());
            Optional<NclCentrocCliente> centrocCliente =
                    nclCentrocClienteRepository.findById(request.getCentrocClienteId());
            if (usuario.isPresent() && centrocCliente.isPresent()) {
                AdmUsuarioXcliente admUsuarioXcliente = new AdmUsuarioXcliente();
                admUsuarioXcliente.setUsuarioId(usuario.get());
                admUsuarioXcliente.setCentrocClienteId(centrocCliente.get());
                admUsuarioXcliente.setEsActivo(Constantes.ESTATUS_ACTIVO);

                respuesta.setDatos(admUsuarioXclienteRepository.save(admUsuarioXcliente));
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
    public RespuestaGenerica quitar(AsignarQuitarRequest request) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (respuesta.isResultado()) {
                Optional<AdmUsuarios> usuario = admUsuariosRepository.findById(request.getUsuarioId());
                Optional<NclCentrocCliente> centrocCliente =
                        nclCentrocClienteRepository.findById(request.getCentrocClienteId());
                if (usuario.isPresent() && centrocCliente.isPresent()) {
                    Optional<AdmUsuarioXcliente> admUsuarioXcliente = admUsuarioXclienteRepository
                            .findByCentrocClienteIdAndUsuarioId(centrocCliente.get(), usuario.get());

                    admUsuarioXcliente.ifPresent(usuarioXcliente ->
                            admUsuarioXclienteRepository.update(usuarioXcliente.getUsuarioXclienteId(),
                                    Utilidades.obtenerFechaSystema(), Constantes.ESTATUS_INACTIVO));

                    respuesta.setDatos(null);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                    respuesta.setMensaje(Constantes.EXITO);
                } else {
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                    respuesta.setMensaje(Constantes.ERROR_ROLPERMISO_NO_EXISTE);
                }
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " quitar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica findClientesByUsuarioIdAndEsActivo(Integer usuarioId, Boolean activo) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            Optional<AdmUsuarios> usuario = admUsuariosRepository.findById(usuarioId);
            if (usuario.isPresent()) {
                respuestaGenerica.setDatos(admUsuarioXclienteRepository
                        .findByUsuarioIdAndEsActivo(usuario.get(), activo));
                /*respuestaGenerica.setDatos(admUsuarioXclienteRepository
                        .findCentrocClienteIdByUsuarioIdAndEsActivo(usuario.get(), activo));*/
                respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
                respuestaGenerica.setMensaje(Constantes.EXITO);
            } else {
                respuestaGenerica.setResultado(Constantes.RESULTADO_ERROR);
                respuestaGenerica.setMensaje(Constantes.ERROR_USUARIO_NO_EXISTE);
            }
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" findClientesByUsuarioIdAndEsActivo" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

    @Override
    public RespuestaGenerica filtrarPaginable(FiltradoRequest request, Integer numeroRegistros, Integer pagina) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            List<AdmUsuarios> usuarios = usuarioRepository.filtrarPaginado(request,numeroRegistros,pagina);

            Set<AdmUsuarios>  setUsuarios = new HashSet<AdmUsuarios>(usuarios);
            usuarios.clear();
            usuarios.addAll(setUsuarios);

            for (AdmUsuarios usuario : usuarios) {
                admRolesRepository.findById(usuario.getRolId().getRolId())
                        .ifPresent(usuario::setRolId);
                usuario.setCentrocClientes(admUsuarioXclienteRepository
                        .findCentrocClienteIdByUsuarioIdAndEsActivo(usuario, Constantes.ESTATUS_ACTIVO));
            }
            AdminUsuarioDto adminUsuario = new AdminUsuarioDto();
            adminUsuario.setUsuarios(usuarios);
            adminUsuario.setTotalRegistros(usuarioRepository.cantidadTotal(request).get(0).getCantidad());
            respuestaGenerica.setDatos(adminUsuario);
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            respuestaGenerica.setMensaje(Constantes.EXITO);
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" findByEsActivo" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

}
