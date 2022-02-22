package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.admin.services.CsTipoDeduccionService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTipoDeduccion;

@Controller("/adminCatalogo/csTipoDeduccion")
public class CsTipoDeduccionController {

    @Inject
    private CsTipoDeduccionService csTipoDeduccionService;

    @Operation(summary = "${cosmonaut.controller.csTipoDeduccion.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.csTipoDeduccion.findByEsActivo.descripcion}",
            operationId = "csTipoDeduccion.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tipo Deducción - Listar todos activo/inactivo.")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(csTipoDeduccionService.findByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.csTipoDeduccion.guardar.resumen}",
            description = "${cosmonaut.controller.csTipoDeduccion.guardar.descripcion}",
            operationId = "csTipoDeduccion.guardar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tipo Deducción  - guardar")
    @Put(value = "/guardar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> guardar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body CsTipoDeduccion csTipoDeduccion) {
        try {
            return HttpResponse.ok(csTipoDeduccionService.guardar(csTipoDeduccion));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.csTipoDeduccion.modificar.resumen}",
            description = "${cosmonaut.controller.csTipoDeduccion.modificar.descripcion}",
            operationId = "csTipoDeduccion.modificar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tipo Deducción  - modificar")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body CsTipoDeduccion csTipoDeduccion) {
        try {
            return HttpResponse.ok(csTipoDeduccionService.modificar(csTipoDeduccion));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.csBanco.listarestatusdescricpcion.resumen}",
            description = "${cosmonaut.controller.csBanco.listarestatusdescricpcion.descripcion}",
            operationId = "csBanco.listarestatusdescricpcion")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tipo Deducción - Listar todos activo/inactivo y descripcion.")
    @Post(value = "/listar/descripcion", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listarEstatusDescripcion(@Body DescripcionRequest descripcion) {
        try {
            return HttpResponse.ok(csTipoDeduccionService.listarEstatusDescripcion(descripcion));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }
}
