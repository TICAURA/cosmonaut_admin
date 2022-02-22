package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.admin.services.ModuloService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

@Controller("/modulos")
public class ModulosController {

    @Inject
    private ModuloService moduloService;

    @Operation(summary = "${cosmonaut.controller.modulos.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.modulos.findByEsActivo.descripcion}",
            operationId = "modulos.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Modulos - Obtener todos activo/inactivo.")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(moduloService.findByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.modulos.findByVersion.resumen}",
            description = "${cosmonaut.controller.modulos.findByVersion.descripcion}",
            operationId = "modulos.findByVersion")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Modulos - Obtener todos activo/inactivo por Version.")
    @Get(value = "version/{id}/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByVersion(@PathVariable @NotNull Integer id, @NotNull @PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(moduloService.findByVersion(id, activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.modulos.findByVersion.resumen}",
            description = "${cosmonaut.controller.modulos.findByVersion.descripcion}",
            operationId = "modulos.findByVersion")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Modulos - Obtener todos activo/inactivo por Version.")
    @Get(value = "dataStudio/getUrl/{id}/{idCliente}/{version}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByUrl(@PathVariable @NotNull Integer id,@PathVariable @NotNull Integer idCliente,@PathVariable @NotNull Integer version) {
        try {
            return HttpResponse.ok(moduloService.findURL(id,idCliente,version));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
