package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import mx.com.ga.cosmonaut.admin.services.CsBancoService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsBanco;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

@Controller("/adminCatalogo/csbanco")
public class CsBancoController {

    @Inject
    private CsBancoService csBancoService;

    @Operation(summary = "${cosmonaut.controller.csBanco.findAll.resumen}",
            description = "${cosmonaut.controller.csBanco.findAll.descripcion}",
            operationId = "csBanco.findAll")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Banco - Listar todos")
    @Get(value = "/listar/todos", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findAll() {
        try {
            return HttpResponse.ok(csBancoService.findAll());
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.csBanco.findByRazonSocial.resumen}",
            description = "${cosmonaut.controller.csBanco.findByRazonSocial.descripcion}",
            operationId = "csBanco.findByRazonSocial")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Banco - Obtener por Razon Social")
    @Get(value = "/obtener/razonSocial/{razonSocial}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByRazonSocial(@NotBlank @PathVariable String razonSocial) {
        try {
            return HttpResponse.ok(csBancoService.findByRazonSocial(razonSocial));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.csBanco.findById.resumen}",
            description = "${cosmonaut.controller.csBanco.findById.descripcion}",
            operationId = "csBanco.findById")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Banco - Obtener por ID")
    @Get(value = "/obtener/id/{id}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findById(@NotBlank @PathVariable Long id) {
        try {
            return HttpResponse.ok(csBancoService.findById(id));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.csBanco.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.csBanco.findByEsActivo.descripcion}",
            operationId = "csBanco.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Banco - Listar todos activo/inactivo.")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(csBancoService.findByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
     @Operation(summary = "${cosmonaut.controller.csBanco.guardar.resumen}",
            description = "${cosmonaut.controller.csBanco.guardar.descripcion}",
             operationId = "csBanco.guardar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Banco - guardar")
    @Put(value = "/guardar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> guardar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body CsBanco csBanco) {
        try {
            return HttpResponse.ok(csBancoService.guardar(csBanco));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.csBanco.modificar.resumen}",
            description = "${cosmonaut.controller.csBanco.modificar.descripcion}",
            operationId = "csBanco.modificar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Banco - modificar")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body CsBanco csBanco) {
        try {
            return HttpResponse.ok(csBancoService.modificar(csBanco));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.csBanco.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.csBanco.findByEsActivo.descripcion}",
            operationId = "csBanco.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Banco - Listar todos activo/inactivo.")
    @Post(value = "/listar/descripcion", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listarEstatusDescricpcion(@Body DescripcionRequest descripcion) {
        try {
            return HttpResponse.ok(csBancoService.listarEstatusDescripcion(descripcion));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }
}
