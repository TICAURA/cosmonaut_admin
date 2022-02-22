package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import mx.com.ga.cosmonaut.admin.services.CatFacultadPoderService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatFacultadPoder;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

@Controller("/adminCatalogo/catFacultadPoder")
public class CatFacultadPoderController {
   
    @Inject
    private CatFacultadPoderService catFacultadPoderService ;

    @Operation(summary = "${cosmonaut.controller.catFacultadPoder.findAll.resumen}",
            description = "${cosmonaut.controller.catFacultadPoder.findAll.descripcion}",
            operationId = "catFacultadPoder.findAll")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Facultad Poder - Listar Todos")
    @Get(value = "/listar/todos", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findAll() {
        try {
            return HttpResponse.ok(catFacultadPoderService.findAll());
        } catch (Exception e) {
            
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.catFacultadPoder.findById.resumen}",
            description = "${cosmonaut.controller.catFacultadPoder.findById.descripcion}",
            operationId = "catFacultadPoder.findById")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Facultad Poder - Obtener por ID")
    @Get(value = "/obtener/id/{id}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findById(@NotBlank @PathVariable Integer id) {
        try {
            return HttpResponse.ok(catFacultadPoderService.findById(id));
        } catch (Exception e) {
            
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.catFacultadPoder.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.catFacultadPoder.findByEsActivo.descripcion}",
            operationId = "catFacultadPoder.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Facultad Poder - Listar Todos activo/inactivo")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(catFacultadPoderService.findEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }


    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.catFacultadPoder.guardaAsentamiento.resumen}",
            description = "${cosmonaut.controller.catFacultadPoder.guardaAsentamiento.descripcion}",
            operationId = "catFacultadPoder.guardaAsentamiento")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Facultad Poder - guardar")
    @Put(value = "/guardar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> guardar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body CatFacultadPoder facultad) {
        try {
            return HttpResponse.ok(catFacultadPoderService.guardar(facultad));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
     @Operation(summary = "${cosmonaut.controller.catFacultadPoder.modificaAsentamiento.resumen}",
            description = "${cosmonaut.controller.catFacultadPoder.modificaAsentamiento.descripcion}",
             operationId = "catFacultadPoder.modificaAsentamiento")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Facultad Poder - modificar")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body CatFacultadPoder facultad) {
        try {
            return HttpResponse.ok(catFacultadPoderService.modificar(facultad));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.csBanco.listarestatusdescricpcion.resumen}",
            description = "${cosmonaut.controller.csBanco.listarestatusdescricpcion.descripcion}",
            operationId = "csBanco.listarestatusdescricpcion")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Facultad Poder - Listar todos activo/inactivo y descripcion.")
    @Post(value = "/listar/descripcion", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listarEstatusDescripcion(@Body DescripcionRequest descripcion) {
        try {
            return HttpResponse.ok(catFacultadPoderService.listarEstatusDescripcion(descripcion));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }
}