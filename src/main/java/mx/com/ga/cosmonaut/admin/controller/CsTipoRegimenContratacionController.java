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
import mx.com.ga.cosmonaut.admin.services.CsTipoRegimenContratacionService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTipoRegimenContratacion;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

@Controller("/adminCatalogo/csTipoRegimenContratacion")
public class CsTipoRegimenContratacionController {

    @Inject
    private CsTipoRegimenContratacionService csTipoRegimenContratacionService;

    @Operation(summary = "${cosmonaut.controller.csTipoRegimenContratacion.findAll.resumen}",
            description = "${cosmonaut.controller.csTipoRegimenContratacion.findAll.descripcion}",
            operationId = "csTipoRegimenContratacion.findAll")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tipo Regimen Contratación - Listar todos")
    @Get(value = "/listar/todos", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findAll() {
        try {
            return HttpResponse.ok(csTipoRegimenContratacionService.findAll());
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.csTipoRegimenContratacion.findById.resumen}",
            description = "${cosmonaut.controller.csTipoRegimenContratacion.findById.descripcion}",
            operationId = "csTipoRegimenContratacion.findById")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tipo Regimen Contratación - Obtener por ID")
    @Get(value = "/obtener/id/{idTipoRegimen}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findById(@NotBlank @PathVariable String idTipoRegimen) {
        try {

            return HttpResponse.ok(csTipoRegimenContratacionService.findById(idTipoRegimen));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.csTipoRegimenContratacion.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.csTipoRegimenContratacion.findByEsActivo.descripcion}",
            operationId = "csTipoRegimenContratacion.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tipo Regimen Contratación - Listar Todos activo/inactivo.")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(csTipoRegimenContratacionService.findByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.csTipoRegimenContratacion.guardar.resumen}",
            description = "${cosmonaut.controller.csTipoRegimenContratacion.guardar.descripcion}",
            operationId = "csTipoRegimenContratacion.guardar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tipo Regimen Contratación  - guardar")
    @Put(value = "/guardar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> guardar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body CsTipoRegimenContratacion csTipoRegimenContratacion) {
        try {
            return HttpResponse.ok(csTipoRegimenContratacionService.guardar(csTipoRegimenContratacion));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.csTipoRegimenContratacion.modificar.resumen}",
            description = "${cosmonaut.controller.csTipoRegimenContratacion.modificar.descripcion}",
            operationId = "csTipoRegimenContratacion.modificar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tipo Regimen Contratación - modificar")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body CsTipoRegimenContratacion csTipoRegimenContratacion) {
        try {
            return HttpResponse.ok(csTipoRegimenContratacionService.modificar(csTipoRegimenContratacion));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.csBanco.listarestatusdescricpcion.resumen}",
            description = "${cosmonaut.controller.csBanco.listarestatusdescricpcion.descripcion}",
            operationId = "csBanco.listarestatusdescricpcion")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tipo Regimen Contratación - Listar todos activo/inactivo y descripcion.")
    @Post(value = "/listar/descripcion", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listarEstatusDescripcion(@Body DescripcionRequest descripcion) {
        try {
            return HttpResponse.ok(csTipoRegimenContratacionService.listarEstatusDescripcion(descripcion));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
