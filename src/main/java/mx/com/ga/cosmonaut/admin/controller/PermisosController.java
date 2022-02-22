package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.admin.services.PermisoService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.permisos.AgregarQuitarRequest;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import javax.validation.Valid;

@Controller("/permisos")
public class PermisosController {

    @Inject
    private PermisoService permisoService;

    @Operation(summary = "${cosmonaut.controller.permisos.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.permisos.findByEsActivo.descripcion}",
            operationId = "permisos.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Permisos - Obtener todos activo/inactivo.")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(permisoService.findByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.permisos.submodulo.resumen}",
            description = "${cosmonaut.controller.permisos.submodulo.descripcion}",
            operationId = "permisos.submodulo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Permisos - Obtener todos los permisos por submodulo.")
    @Get(value = "/submodulo/{id}/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findBySubmoduloIdAndEsActivo(@PathVariable Integer id, @PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(permisoService.findBySubmoduloIdAndEsActivo(id, activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.permisos.agregar.resumen}",
            description = "${cosmonaut.controller.permisos.agregar.descripcion}",
            operationId = "permisos.agregar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Permisos - Agrega los permisos a un rol.")
    @Put(value = "/agregar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> agregar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body @Valid AgregarQuitarRequest request) {
        try {
            return HttpResponse.ok(permisoService.agregar(request));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.permisos.quitar.resumen}",
            description = "${cosmonaut.controller.permisos.quitar.descripcion}",
            operationId = "permisos.quitar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Permisos - Quita los permisos a un rol.")
    @Post(value = "/quitar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> quitar(@Header("datos-flujo") String datosFlujo,
                                                  @Header("datos-sesion") String datosSesion,
                                                  @Body @Valid AgregarQuitarRequest request) {
        try {
            return HttpResponse.ok(permisoService.quitar(request));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.permisos.rol.resumen}",
            description = "${cosmonaut.controller.permisos.rol.descripcion}",
            operationId = "permisos.rol")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Permisos - Lista los permisos de un rol.")
    @Get(value = "/rol/{id}/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByRol(@PathVariable Integer id, @PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(permisoService.findByRol(id, activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
