package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.admin.services.RolService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.roles.GuardarRequest;
import mx.com.ga.cosmonaut.common.dto.administracion.roles.ModificarRequest;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import javax.validation.Valid;

@Controller("/rol")
public class RolesController {

    @Inject
    private RolService rolService;

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.rol.guardar.resumen}",
            description = "${cosmonaut.controller.rol.guardar.descripcion}",
            operationId = "rol.guardar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Roles - Guardar.")
    @Put(value = "/guardar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> guardar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body @Valid GuardarRequest request) {
        try {
            return HttpResponse.ok(rolService.guardar(request));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.rol.modificar.resumen}",
            description = "${cosmonaut.controller.rol.modificar.descripcion}",
            operationId = "rol.modificar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Roles - Modificar.")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body @Valid ModificarRequest request) {
        try {
            return HttpResponse.ok(rolService.modificar(request));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.rol.eliminar.resumen}",
            description = "${cosmonaut.controller.rol.eliminar.descripcion}",
            operationId = "rol.eliminar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Roles - Eliminar.")
    @Delete(value = "/eliminar/{id}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> eliminar(@Header("datos-flujo") String datosFlujo,
                                                    @Header("datos-sesion") String datosSesion,
                                                    @PathVariable Integer id) {
        try {
            return HttpResponse.ok(rolService.eliminar(id));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.rol.obtenerid.resumen}",
            description = "${cosmonaut.controller.rol.obtenerid.descripcion}",
            operationId = "rol.obtenerid")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Roles - Obtener.")
    @Get(value = "/obtener/id/{id}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> obtenerId(@PathVariable Integer id) {
        try {
            return HttpResponse.ok(rolService.obtenerId(id));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.rol.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.rol.findByEsActivo.descripcion}",
            operationId = "rol.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Roles - Obtener todos activo/inactivo.")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(rolService.findByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.rol.findByCentrocClientesCentrocClienteIdAndEsActivo.resumen}",
            description = "${cosmonaut.controller.rol.findByCentrocClientesCentrocClienteIdAndEsActivo.descripcion}",
            operationId = "rol.findByCentrocClientesCentrocClienteIdAndEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Roles - Obtener Roles por cliente.")
    @Get(value = "/cliente/{id}/version/{idVersion}/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByCentrocClientesCentrocClienteIdAndEsActivo(
            @PathVariable Integer id, @PathVariable Integer idVersion, @PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(rolService.findByCentrocClientesCentrocClienteIdAndEsActivo(id, idVersion, activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.rol.listByRolIdRolIdAndCentrocClienteId.resumen}",
            description = "${cosmonaut.controller.rol.listByRolIdRolIdAndCentrocClienteId.descripcion}",
            operationId = "rol.listByRolIdRolIdAndCentrocClienteId")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Roles - Obtener Listado de usuarios por roles.")
    @Get(value = "/cliente/{id}/version/{idVersion}/rol/{idRol}/listar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listByRolIdRolIdAndCentrocClienteId(
            @PathVariable Integer id, @PathVariable Integer idVersion, @PathVariable Integer idRol) {
        try {
            return HttpResponse.ok(rolService.listByRolIdRolIdAndCentrocClienteId(id,idVersion, idRol));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }


}
