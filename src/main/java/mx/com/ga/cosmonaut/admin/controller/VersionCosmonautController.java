package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.admin.services.VersionCosmonautService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.preferencias.PreferenciasColores;
import mx.com.ga.cosmonaut.common.dto.administracion.version.AsignarQuitarRequest;
import mx.com.ga.cosmonaut.common.dto.administracion.version.ModificarRequest;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import javax.validation.Valid;

@Controller("/version")
public class VersionCosmonautController {

    @Inject
    private VersionCosmonautService versionCosmonautService;

    @Operation(summary = "${cosmonaut.controller.version.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.version.findByEsActivo.descripcion}",
            operationId = "version.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Version - Obtener todos activo/inactivo.")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(versionCosmonautService.findByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.version.findSubmodulosByVersionIdAndEsActivo.resumen}",
            description = "${cosmonaut.controller.version.findSubmodulosByVersionIdAndEsActivo.descripcion}",
            operationId = "version.findSubmodulosByVersionIdAndEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Version - Obtener todos activo/inactivo.")
    @Get(value = "/{id}/listar/submodulos/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findSubmodulosByVersionIdAndEsActivo(@PathVariable Integer id, @PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(versionCosmonautService.findSubmodulosByVersionIdAndEsActivo(id, activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.version.asignar.resumen}",
            description = "${cosmonaut.controller.version.asignar.descripcion}",
            operationId = "version.asignar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Version - Asignar.")
    @Put(value = "/asignar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> asignar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body @Valid AsignarQuitarRequest request,@Body PreferenciasColores preferencias ) {
        try {
            return HttpResponse.ok(versionCosmonautService.asignar(request,preferencias));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.version.modificar.resumen}",
            description = "${cosmonaut.controller.version.modificar.descripcion}",
            operationId = "version.modificar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Version - Modificar.")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body @Valid ModificarRequest request,@Body PreferenciasColores preferencias ) {
        try {
            return HttpResponse.ok(versionCosmonautService.modificar(request,preferencias));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.version.findVersionByClienteId.resumen}",
            description = "${cosmonaut.controller.version.findVersionByClienteId.descripcion}",
            operationId = "findVersionByClienteId")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Version - Obtener version cosmonaut por cliente.")
    @Get(value = "/cliente/{clienteId}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findVersionByClienteId(@PathVariable Integer clienteId) {
        try {
            return HttpResponse.ok(versionCosmonautService.findVersionByClienteId(clienteId));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.version.findAll.resumen}",
            description = "${cosmonaut.controller.version.findAll.descripcion}",
            operationId = "version.findAll")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Version - Obtener version, submodulo y permiso.")
    @Get(value = "/submodulo/permiso", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findAll() {
        try {
            return HttpResponse.ok(versionCosmonautService.findAll());
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
