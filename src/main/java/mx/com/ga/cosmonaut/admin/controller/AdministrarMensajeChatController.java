package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.admin.services.AdministrarMensajeChatService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmMensajeChatCentrocostos;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

@Controller("/administrar-mensaje-chat")
public class AdministrarMensajeChatController {

    @Inject
    private AdministrarMensajeChatService administrarMensajeChatService;

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.administrarmensajechat.guardar.resumen}",
            description = "${cosmonaut.controller.administrarmensajechat.guardar.descripcion}",
            operationId = "administrarmensajechat.guardar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Administrar Mensaje Chat - Guardar.")
    @Put(value = "/guardar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> guardar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body AdmMensajeChatCentrocostos mensajeChatCentrocostos) {
        try {
            return HttpResponse.ok(administrarMensajeChatService.guardar(mensajeChatCentrocostos));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.administrarmensajechat.modificar.resumen}",
            description = "${cosmonaut.controller.administrarmensajechat.modificar.descripcion}",
            operationId = "administrarmensajechat.modificar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Administrar Mensaje Chat - Modificar")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body AdmMensajeChatCentrocostos mensajeChatCentrocostos) {
        try {
            return HttpResponse.ok(administrarMensajeChatService.modificar(mensajeChatCentrocostos));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.administrarmensajechat.obtenerid.resumen}",
            description = "${cosmonaut.controller.administrarmensajechat.obtenerid.descripcion}",
            operationId = "administrarmensajechat.obtenerid")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Administrar Mensaje Chat - Obtener por ID")
    @Get(value = "/obtener/id/{id}",consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> obtenerId(@NotBlank @PathVariable Integer id){
        try {
            return HttpResponse.ok(administrarMensajeChatService.obtenerId(id));
        }catch (Exception e){
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.administrarmensajechat.listarempresa.resumen}",
            description = "${cosmonaut.controller.administrarmensajechat.listarempresa.descripcion}",
            operationId = "administrarmensajechat.listarempresa")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Administrar Mensaje Chat - Listar")
    @Get(value = "/lista/empresa/{idEmpresa}",consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listarEmpresa(@NotBlank @PathVariable Integer idEmpresa){
        try {
            return HttpResponse.ok(administrarMensajeChatService.listarEmpresa(idEmpresa));
        }catch (Exception e){
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.administrarmensajechat.listarempresausuario.resumen}",
            description = "${cosmonaut.controller.administrarmensajechat.listarempresausuario.descripcion}",
            operationId = "administrarmensajechat.listarempresausuario")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Administrar Mensaje Chat - Listar")
    @Get(value = "/lista/empresa/usuario/{idEmpresa}/{idUsuario}",consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listarEmpresaUsuario(@NotBlank @PathVariable Integer idEmpresa,
                                                         @NotBlank @PathVariable Integer idUsuario){
        try {
            return HttpResponse.ok(administrarMensajeChatService.listarEmpresaUsuario(idEmpresa, idUsuario));
        }catch (Exception e){
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
