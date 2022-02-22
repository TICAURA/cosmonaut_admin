package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.admin.services.UsuariosService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.usuarios.*;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import javax.validation.Valid;

@Controller("/usuarios")
public class UsuariosController {

    @Inject
    private UsuariosService usuariosService;

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.usuarios.guardar.resumen}",
            description = "${cosmonaut.controller.usuarios.guardar.descripcion}",
            operationId = "tablaValorReferencia.guardar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Guardar.")
    @Put(value = "/guardar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> guardar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body @Valid GuardarRequest request) {
        try {
            return HttpResponse.ok(usuariosService.guardar(request));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.usuarios.modificar.resumen}",
            description = "${cosmonaut.controller.usuarios.modificar.descripcion}",
            operationId = "usuarios.modificar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Modificar.")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body @Valid ModificarRequest request) {
        try {
            return HttpResponse.ok(usuariosService.modificar(request));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.usuarios.cambiarPwd.resumen}",
            description = "${cosmonaut.controller.usuarios.cambiarPwd.descripcion}",
            operationId = "usuarios.cambiarPwd")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Cambio de contraseña.")
    @Post(value = "/cambiarPwd", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> cambiarPwd(@Body @Valid CambiarPwdRequest request) {
        try {
            return HttpResponse.ok(usuariosService.cambiarPwd(request));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.usuarios.reestablecerPwd.resumen}",
            description = "${cosmonaut.controller.usuarios.reestablecerPwd.descripcion}",
            operationId = "usuarios.reestablecerPwd")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Reestablecimiento de la contraseña.")
    @Post(value = "/reestablecerPwd", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> reestablecerPwd(@Body ReestablecerPwdRequest request) {
        try {
            return HttpResponse.ok(usuariosService.reestablecerPwd(request));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.usuarios.eliminar.resumen}",
            description = "${cosmonaut.controller.usuarios.eliminar.descripcion}",
            operationId = "usuarios.eliminar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Eliminar.")
    @Delete(value = "/eliminar/{id}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> eliminar(@Header("datos-flujo") String datosFlujo,
                                                    @Header("datos-sesion") String datosSesion,
                                                    @PathVariable Integer id) {
        try {
            return HttpResponse.ok(usuariosService.eliminar(id));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.usuarios.cambiarestados.resumen}",
            description = "${cosmonaut.controller.usuarios.cambiarestados.descripcion}",
            operationId = "usuarios.cambiarestados")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Cambiar estados.")
    @Post(value = "/cambiar/estados", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> cambiarEstados(@Header("datos-flujo") String datosFlujo,
                                                          @Header("datos-sesion") String datosSesion,
                                                          @Body @Valid CambiarEstadosRequest request) {
        try {
            return HttpResponse.ok(usuariosService.cambiarEstados(request));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.usuarios.obtenerid.resumen}",
            description = "${cosmonaut.controller.usuarios.obtenerid.descripcion}",
            operationId = "usuarios.obtenerid")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Obtener.")
    @Get(value = "/obtener/id/{id}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> obtenerId(@PathVariable Integer id) {
        try {
            return HttpResponse.ok(usuariosService.obtenerId(id));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.usuarios.obtenerusername.resumen}",
            description = "${cosmonaut.controller.usuarios.obtenerusername.descripcion}",
            operationId = "usuarios.obtenerusername")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Obtener por username.")
    @Get(value = "/obtener/username/{username}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> obtenerUsername(@PathVariable String username) {
        try {
            return HttpResponse.ok(usuariosService.obtenerUsername(username));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.usuarios.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.usuarios.findByEsActivo.descripcion}",
            operationId = "usuarios.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Obtener todos activo/inactivo.")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(usuariosService.findByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.usuarios.filtrar.resumen}",
            description = "${cosmonaut.controller.usuarios.filtrar.descripcion}",
            operationId = "usuarios.filtrar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Filtrar.")
    @Post(value = "/filtrar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> filtrar(@Body @Valid FiltradoRequest request) {
        try {
            return HttpResponse.ok(usuariosService.filtrar(request));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.usuarios.asignar.resumen}",
            description = "${cosmonaut.controller.usuarios.asignar.descripcion}",
            operationId = "usuarios.asignar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Asignar.")
    @Put(value = "/asignar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> asignar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body @Valid AsignarQuitarRequest request) {
        try {
            return HttpResponse.ok(usuariosService.asignar(request));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.usuarios.quitar.resumen}",
            description = "${cosmonaut.controller.usuarios.quitar.descripcion}",
            operationId = "usuarios.quitar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Quitar.")
    @Post(value = "/quitar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> quitar(@Header("datos-flujo") String datosFlujo,
                                                  @Header("datos-sesion") String datosSesion,
                                                  @Body @Valid AsignarQuitarRequest request) {
        try {
            return HttpResponse.ok(usuariosService.quitar(request));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.usuarios.findClientesByUsuarioIdAndEsActivo.resumen}",
            description = "${cosmonaut.controller.usuarios.findClientesByUsuarioIdAndEsActivo.descripcion}",
            operationId = "usuarios.findClientesByUsuarioIdAndEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Obtener los clientes activo/inactivo por usuario.")
    @Get(value = "{usuarioId}/clientes/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findClientesByUsuarioIdAndEsActivo(@PathVariable Integer usuarioId,
                                                                  @PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(usuariosService.findClientesByUsuarioIdAndEsActivo(usuarioId, activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.usuarios.filtrarpaginado.resumen}",
            description = "${cosmonaut.controller.usuarios.filtrarpaginado.descripcion}",
            operationId = "usuarios.filtrarpaginado")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Usuarios - Filtrar.")
    @Post(value = "/filtrar/paginado/{numeroRegistros}/{pagina}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> filtrarPaginado(@Body @Valid FiltradoRequest filtrado,
                                                           @PathVariable Integer numeroRegistros,
                                                           @PathVariable  Integer pagina) {
        try {
            return HttpResponse.ok(usuariosService.filtrarPaginable(filtrado,numeroRegistros, pagina));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
