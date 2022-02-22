package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import mx.com.ga.cosmonaut.admin.services.AdmNoticiaService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.noticias.AdmNoticiasDto;
import mx.com.ga.cosmonaut.common.entity.administracion.noticias.AdmNoticias;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;


@Controller("/noticias")
public class AdminNoticiasController {

    @Inject
    private AdmNoticiaService admNoticiaService;


    @Get(value = "/{clienteid}/empresapadre/{clienteIdPadre}/empleado/{idPersona}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> consultarNoticias(@PathVariable("clienteid") Integer clienteId,@PathVariable("clienteIdPadre") Integer clienteIdPadre,@PathVariable("idPersona") Integer personaId, @Parameter("zonaHoraria")String zonaHoraria){
        try {
            return HttpResponse.ok(admNoticiaService.getNoticias(clienteId,clienteIdPadre,personaId,zonaHoraria));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Get(value = "/detalle/{idNoticia}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> consultarNoticiasdetalle(@PathVariable("idNoticia") Integer idNoticia){
        try {
            return HttpResponse.ok(admNoticiaService.getNoticiaDetalle(idNoticia));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Post(value = "/", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> agregarNoticia(@Header("datos-flujo") String datosFlujo,
                                                          @Header("datos-sesion") String datosSesion,
                                                          @Body AdmNoticiasDto noticia){
        try {
            return HttpResponse.ok(admNoticiaService.agregarNoticia(noticia));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Put(value = "/", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificarNoticia(@Header("datos-flujo") String datosFlujo,
                                                            @Header("datos-sesion") String datosSesion,
                                                            @Body AdmNoticiasDto noticia){
        try {
            return HttpResponse.ok(admNoticiaService.modificarNoticia(noticia));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Delete(value = "/{idNoticia}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> eliminarNoticia(@Header("datos-flujo") String datosFlujo,
                                                           @Header("datos-sesion") String datosSesion,
                                                           @PathVariable("idNoticia") Integer id){
        try {
            return HttpResponse.ok(admNoticiaService.eliminarNoticia(id));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }


    @Get(value = "/categorias", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> obtenerCategorias(){
        try {
            return HttpResponse.ok(admNoticiaService.getListaCategorias());
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }


    @Get(value = "/cosmonaut", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> getNoticiasCosmonaut(){
        try {
            return HttpResponse.ok(admNoticiaService.getNoticiasCosmonaut());
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }
    @Get(value = "/cliente/{idCliente}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> getNoticiaPorCliente(@PathVariable("idCliente")Integer clienteId){
        try {
            return HttpResponse.ok(admNoticiaService.getNoticiasCliente(clienteId));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }
    @Get(value = "/empresa/{idEmpresa}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> getNoticiaPorEmpresa(@PathVariable("idEmpresa")Integer idEmpresa){
        try {
            return HttpResponse.ok(admNoticiaService.getNoticiasEmpresa(idEmpresa));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }






}
