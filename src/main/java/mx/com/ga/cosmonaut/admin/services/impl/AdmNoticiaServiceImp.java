package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.AdmNoticiaService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.RespuestaGoogleStorage;
import mx.com.ga.cosmonaut.common.dto.administracion.noticias.AdmNoticiasConsulta;
import mx.com.ga.cosmonaut.common.dto.administracion.noticias.AdmNoticiasDto;
import mx.com.ga.cosmonaut.common.dto.administracion.noticias.AdmNoticiasResponse;
import mx.com.ga.cosmonaut.common.entity.administracion.noticias.AdmCategoriaNoticias;
import mx.com.ga.cosmonaut.common.entity.administracion.noticias.AdmNoticias;
import mx.com.ga.cosmonaut.common.entity.administracion.noticias.AdmNoticiasxCliente;
import mx.com.ga.cosmonaut.common.entity.administracion.noticias.AdmNoticiasxPersona;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmUsuarios;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmVersionCosmonautXcliente;
import mx.com.ga.cosmonaut.common.entity.cliente.NclCentrocCliente;
import mx.com.ga.cosmonaut.common.entity.colaborador.ContratoColaborador;
import mx.com.ga.cosmonaut.common.entity.colaborador.NcoContratoColaborador;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.administracion.noticias.*;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmVersionCosmonautXclienteRepository;
import mx.com.ga.cosmonaut.common.repository.colaborador.NcoContratoColaboradorRepository;
import mx.com.ga.cosmonaut.common.repository.nativo.ContratoColaboradorRepository;
import mx.com.ga.cosmonaut.common.service.AdmUsuariosService;
import mx.com.ga.cosmonaut.common.service.GoogleStorageService;
import mx.com.ga.cosmonaut.common.util.Constantes;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class AdmNoticiaServiceImp implements AdmNoticiaService {

    @Inject
    private AdmNoticiasRepository admNoticiasRepository;

    @Inject
    private AdmNoticiasNativoRepository admNoticiasNativoRepository;
    @Inject
    private AdmCatalogosNoticiasRepository admCatalogosNoticiasRepository;

    @Inject
    private AdmNoticasxClienteRepository admNoticiasxClienteRepository;


    @Inject
    private AdmNoticiasxPersonaRepository admNoticiaxPersonaRepository;

    @Inject
    private GoogleStorageService googleStorageService;

    @Inject
    private AdmVersionCosmonautXclienteRepository admVersionRespository;
    @Inject
    private NcoContratoColaboradorRepository ncoContratoColaboradorRepository;


    @Override
    public RespuestaGenerica agregarNoticia(AdmNoticiasDto parametro) throws ServiceException {
        RespuestaGenerica respuesta = this.camposObligatorios(parametro);
        if(!respuesta.isResultado()){
            return respuesta;
        }

        if(parametro.getImagen() != null)
        {
            UUID objetoRandom = UUID.randomUUID();
            String rutaEspecial = "noticias/empresa"+parametro.getCentrocClienteId()+"/"+objetoRandom;
            RespuestaGoogleStorage respuestastorage =  googleStorageService.subirArchivo(parametro.getImagen(),rutaEspecial);
            parametro.setRutaBucket(rutaEspecial);
            parametro.setThumbnail(respuestastorage.getUrl());
        }

        AdmNoticias noticias = admNoticiasRepository.save(construirNoticia(parametro,false));

       if(!parametro.isTodos()){
           parametro.getClientesId().stream().forEach(s ->{
               AdmNoticiasxCliente cliente = new AdmNoticiasxCliente();
               cliente.setCentrocClienteId(s);
               cliente.setNoticiaId(noticias.getNoticiaId());
               admNoticiasxClienteRepository.save(cliente);
           });
       }
       if(!parametro.isTodosEmpleados()){
           if( parametro.getGrupoNominaId() == null){
               parametro.getPersonasId().stream().forEach(s ->{
                   AdmNoticiasxPersona persona = new AdmNoticiasxPersona();
                   persona.setPersonaId(s);
                   persona.setNoticiaId(noticias.getNoticiaId());
                   admNoticiaxPersonaRepository.save(persona);
               });
           }

       }
        respuesta.setDatos(noticias);
        respuesta.setMensaje(Constantes.EXITO);
        respuesta.setResultado(Constantes.RESULTADO_EXITO);
        return respuesta;
    }

    @Override
    public RespuestaGenerica modificarNoticia(AdmNoticiasDto parametro) throws ServiceException {
        RespuestaGenerica respuesta = this.camposObligatorios(parametro);
        if(!respuesta.isResultado()){
            return respuesta;
        }
        if(parametro.getImagen() != null )
        {
            UUID objetoRandom = UUID.randomUUID();
            String rutaEspecial = "noticias/empresa"+parametro.getUsuarioId()+"/"+objetoRandom;
            RespuestaGoogleStorage respuestastorage =  googleStorageService.subirArchivo(parametro.getImagen(),rutaEspecial);
            parametro.setRutaBucket(rutaEspecial);
            parametro.setThumbnail(respuestastorage.getUrl());
        }
        AdmNoticias noticias = construirNoticia(parametro,true);
        admNoticiasxClienteRepository.deleteByNoticiaId(noticias.getNoticiaId());
        admNoticiaxPersonaRepository.deleteByNoticiaId(noticias.getNoticiaId());




        respuesta.setDatos(admNoticiasRepository.update(noticias));
        if(!parametro.isTodos()){
            parametro.getClientesId().stream().forEach(s ->{
                AdmNoticiasxCliente cliente = new AdmNoticiasxCliente();
                cliente.setCentrocClienteId(s);
                cliente.setNoticiaId(noticias.getNoticiaId());
                admNoticiasxClienteRepository.save(cliente);
            });
        }

        if(!parametro.isTodosEmpleados()){
            if( parametro.getGrupoNominaId() == null){
                parametro.getPersonasId().stream().forEach(s ->{
                    AdmNoticiasxPersona persona = new AdmNoticiasxPersona();
                    persona.setPersonaId(s);
                    persona.setNoticiaId(noticias.getNoticiaId());
                    admNoticiaxPersonaRepository.save(persona);
                });
            }

        }
        respuesta.setMensaje(Constantes.EXITO);
        respuesta.setResultado(Constantes.RESULTADO_EXITO);
        return respuesta;
    }

    @Override
    public RespuestaGenerica eliminarNoticia(Integer idNoticia) {
        RespuestaGenerica respuesta = new RespuestaGenerica();
        respuesta.setMensaje(Constantes.EXITO);
        respuesta.setResultado(Constantes.RESULTADO_EXITO);
        admNoticiaxPersonaRepository.deleteByNoticiaId(idNoticia);
        admNoticiasxClienteRepository.deleteByNoticiaId(idNoticia);
        admNoticiasRepository.deleteById(idNoticia);
        return respuesta;
    }

    @Override
    public RespuestaGenerica getNoticias(Integer clienteId,Integer clienteIdPadre,Integer personaId,String zonaHoraria) throws ServiceException, ParseException {
        RespuestaGenerica respuesta = new RespuestaGenerica();
        List<Integer> listaConsulta = new ArrayList<>();
        List<AdmNoticias> listaFinal = new ArrayList<>();


        List<AdmNoticiasConsulta> listaAdministradoresCosmonaut = this.admNoticiasNativoRepository.consultarClientesAdministradorCosmonaut();
        listaAdministradoresCosmonaut.stream().forEach(o -> listaConsulta.add(o.getCentrocClienteId()));
        listaConsulta.add(clienteId);
        listaConsulta.add(clienteIdPadre);
        List<AdmNoticias> listaTodos = this.admNoticiasRepository.findByClienteIdInList(listaConsulta);
        for(AdmNoticias item : listaTodos){

            if(item.getClienteId().equals(clienteId)){
                if(item.isTodosEmpleados() ){
                    listaFinal.add(item);
                }else if(!item.isTodosEmpleados() && item.getGrupoNominaId() != null){
                    NcoContratoColaborador contrato =  ncoContratoColaboradorRepository.findByPersonaIdPersonaId(personaId);
                    if(contrato.getGrupoNominaId().getGrupoNominaId().equals(item.getGrupoNominaId())){
                        listaFinal.add(item);
                    }
                }
            }else{
                if(item.isTodos()){
                    listaFinal.add(item);
                }
            }


        }

        List<Integer> personasId = new ArrayList<Integer>();
        personasId.add(personaId);

        listaFinal.addAll(this.admNoticiasRepository.findByCentrocClienteIdCentrocClienteIdInList(listaConsulta));
        listaFinal.addAll(this.admNoticiasRepository.findByPersonasIdPersonaIdInList(personasId));


        for(AdmNoticias item : listaFinal){
             if(item.getImagen() != null){
                 URL url =   googleStorageService.obtenerArchivo(item.getImagen()).getUrl();
                 item.setThumbnail(String.format("%1$s://%2$s%3$s?%4$s",url.getProtocol(),url.getHost(),url.getPath(),url.getQuery()));
             }
        }

        AdmNoticiasResponse listaNoticias = new AdmNoticiasResponse();


        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
        isoFormat.setTimeZone(TimeZone.getTimeZone(zonaHoraria));
        String fecha1 = isoFormat.format(new Date().getTime());

        Date fecha  = isoFormat.parse(fecha1);


        //Esta sentencia se ocupa porque comparo 2 fechas iguales pero porque esta la hora de por medio lo toma como distintas jeje..
        SimpleDateFormat dateNormalStr = new SimpleDateFormat("yyyy-MM-dd");


        listaNoticias.setNoticiasCosmonaut(listaFinal.stream()
                .filter(s -> !s.getClienteId().equals(clienteId) && !s.getClienteId().equals(clienteIdPadre)
                            && ((fecha).after(s.getFechaInicio())
                            || dateNormalStr.format(s.getFechaInicio().getTime()).equals(fecha1))
                            && ((fecha).before(s.getFechaFin())
                            ||dateNormalStr.format((s.getFechaFin().getTime())).equals(fecha1))
                        ).collect(Collectors.toList()));

        listaNoticias.setNoticiasGeneral(listaFinal.stream()
                .filter(s -> s.getClienteId().equals(clienteId) ||  s.getClienteId().equals(clienteIdPadre)
                        && ((fecha).after(s.getFechaInicio())
                        || dateNormalStr.format(s.getFechaInicio().getTime()).equals(fecha1))
                        && ((fecha).before(s.getFechaFin())
                        || dateNormalStr.format(s.getFechaFin().getTime()).equals(fecha1))
                ).collect(Collectors.toList()));




     if(!listaFinal.isEmpty()){
         respuesta.setDatos(listaNoticias);
         respuesta.setMensaje(Constantes.EXITO);
         respuesta.setResultado(Constantes.RESULTADO_EXITO);
     }else{
         respuesta.setMensaje(Constantes.MENSAJE_SIN_RESULTADOS);
         respuesta.setResultado(Constantes.RESULTADO_ERROR);
     }
       return respuesta;
    }

    public RespuestaGenerica getListaCategorias(){
        RespuestaGenerica respuesta = new RespuestaGenerica();
        respuesta.setDatos(this.admCatalogosNoticiasRepository.findAll());
        respuesta.setMensaje(Constantes.EXITO);
        respuesta.setResultado(Constantes.RESULTADO_EXITO);
        return respuesta;
    }

    @Override
    public RespuestaGenerica getNoticiaDetalle(Integer idnoticia) throws ServiceException {
        RespuestaGenerica respuesta = new RespuestaGenerica();

        Optional<AdmNoticias> noticia = this.admNoticiasRepository.findById(idnoticia);
        if(noticia.isPresent()){
                AdmNoticias noticiaObj = noticia.get();
               if(noticiaObj.getImagen() != null ){
                   RespuestaGoogleStorage storage = this.googleStorageService.obtenerArchivo(noticiaObj.getImagen());
                   noticiaObj.setThumbnail(String.format("%1$s://%2$s%3$s?%4$s",storage.getUrl().getProtocol(),storage.getUrl().getHost(),storage.getUrl().getPath(),storage.getUrl().getQuery()));
                   noticiaObj.setImagen(Base64.getEncoder().encodeToString(storage.getArreglo()));
               }

               if(noticiaObj.isTodos()){
                    noticiaObj.setCentrocClienteId(new HashSet<>());
               }

                respuesta.setDatos(noticiaObj);
        }

        respuesta.setMensaje(Constantes.EXITO);
        respuesta.setResultado(Constantes.RESULTADO_EXITO);
        return respuesta;
    }

    @Override
    public RespuestaGenerica getNoticiasCosmonaut() throws ServiceException {
        RespuestaGenerica respuesta = new RespuestaGenerica();
        List<AdmNoticiasConsulta> lista = this.admNoticiasNativoRepository.consultarClientesAdministradorCosmonaut();
        List<Integer> listaConsulta = new ArrayList<>();
        lista.stream().forEach(o -> listaConsulta.add(o.getCentrocClienteId()));
        List<AdmNoticias> listaFinal = this.admNoticiasRepository.findByClienteIdInList(listaConsulta);
        for(AdmNoticias item : listaFinal){
            if(item.getImagen() != null){
                URL url =   googleStorageService.obtenerArchivo(item.getImagen()).getUrl();
                item.setThumbnail(String.format("%1$s://%2$s%3$s?%4$s",url.getProtocol(),url.getHost(),url.getPath(),url.getQuery()));
            }
        }
        respuesta.setMensaje(Constantes.EXITO);
        respuesta.setResultado(Constantes.RESULTADO_EXITO);
        respuesta.setDatos(listaFinal);
        return respuesta;
    }

    @Override
    public RespuestaGenerica getNoticiasCliente(Integer clienteId) throws ServiceException {
        RespuestaGenerica respuesta = new RespuestaGenerica();
        List<Integer> listaConsulta = new ArrayList<>();
       // List<AdmNoticiasConsulta> lista = this.admNoticiasNativoRepository.consultarEmpresaByCliente(clienteId);
        //lista.stream().forEach(o -> listaConsulta.add(o.getCentrocClienteId()));
        listaConsulta.add(clienteId);
        List<AdmNoticias> listaFinal = this.admNoticiasRepository.findByClienteIdInList(listaConsulta);

        for(AdmNoticias item : listaFinal){
          if(item.getImagen() != null){
              URL url =   googleStorageService.obtenerArchivo(item.getImagen()).getUrl();
              item.setThumbnail(String.format("%1$s://%2$s%3$s?%4$s",url.getProtocol(),url.getHost(),url.getPath(),url.getQuery()));
          }

        }
        respuesta.setMensaje(Constantes.EXITO);
        respuesta.setResultado(Constantes.RESULTADO_EXITO);
        respuesta.setDatos(listaFinal);
        return respuesta;
    }

    @Override
    public RespuestaGenerica getNoticiasEmpresa(Integer clienteId) throws ServiceException {
        RespuestaGenerica respuesta = new RespuestaGenerica();
        List<Integer> listaConsulta = new ArrayList<>();
        listaConsulta.add(clienteId);
        List<AdmNoticias> listaFinal = this.admNoticiasRepository.findByClienteIdInList(listaConsulta);
        for(AdmNoticias item : listaFinal){
           if(item.getImagen() != null){
               URL url =   googleStorageService.obtenerArchivo(item.getImagen()).getUrl();
               item.setThumbnail(String.format("%1$s://%2$s%3$s?%4$s",url.getProtocol(),url.getHost(),url.getPath(),url.getQuery()));
           }
        }
        respuesta.setMensaje(Constantes.EXITO);
        respuesta.setResultado(Constantes.RESULTADO_EXITO);
        respuesta.setDatos(listaFinal);
        return respuesta;
    }


    private RespuestaGenerica camposObligatorios(AdmNoticiasDto noticia){
        RespuestaGenerica respuesta = new RespuestaGenerica();
        if(invalido(noticia.getTitulo())){
            respuesta.setResultado(Constantes.RESULTADO_ERROR);
            respuesta.setMensaje(Constantes.CAMPOS_REQUERIDOS);
        }else{
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            respuesta.setMensaje(Constantes.EXITO);
        }
        return respuesta;
    }

    private boolean invalido(String mensaje){
        boolean respuesta = false;
        if(mensaje != null){
            respuesta = mensaje.trim().equalsIgnoreCase("");
        }else {
            respuesta = true;
        }
        return respuesta;
    }


    private AdmNoticias construirNoticia(AdmNoticiasDto parametro,boolean esModificar){
        AdmNoticias noticia = new AdmNoticias();
        noticia.setNoticiaId(parametro.getNoticiaId());
        noticia.setUsuarioId(parametro.getUsuarioId());
        Set<NclCentrocCliente> clientesId = new HashSet<NclCentrocCliente>();
        noticia.setTitulo(parametro.getTitulo());
        noticia.setSubtitulo(parametro.getSubtitulo());
        AdmCategoriaNoticias categorias = new AdmCategoriaNoticias();
        categorias.setCategoriaNoticiaId(parametro.getCategoriaId().getCategoriaNoticiaId());
        noticia.setCategoriaId(categorias);
        noticia.setContenido(parametro.getContenido());
        noticia.setFechaInicio(parametro.getFechaInicio());
        noticia.setFechaFin(parametro.getFechaFin());
        noticia.setEsActivo(esModificar?parametro.isEsActivo():true);
        noticia.setClienteId(parametro.getCentrocClienteId());
        noticia.setImagen(parametro.getRutaBucket());
        noticia.setTodos(parametro.isTodos());
        noticia.setTodosEmpleados(parametro.isTodosEmpleados());
        noticia.setGrupoNominaId(parametro.getGrupoNominaId());
        noticia.setEnlace(parametro.getEnlace());
        if(parametro.getThumbnail() != null){
            noticia.setThumbnail(String.format("%1$s://%2$s%3$s?%4$s",parametro.getThumbnail().getProtocol(),parametro.getThumbnail().getHost(),parametro.getThumbnail().getPath(),parametro.getThumbnail().getQuery()));
        }
        return noticia;
    }
}
