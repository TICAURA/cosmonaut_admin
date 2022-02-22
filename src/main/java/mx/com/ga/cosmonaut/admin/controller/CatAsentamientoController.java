
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
import mx.com.ga.cosmonaut.admin.services.CatAsentamientoService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.ubicacion.CatAsentamiento;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

@Controller("/adminCatalogo/catAsentamiento")
public class CatAsentamientoController {

    @Inject
    private CatAsentamientoService catAsentamientoService;

    @Operation(summary = "${cosmonaut.controller.CatAsentamiento.findById.resumen}",
            description = "${cosmonaut.controller.CatAsentamiento.findById.descripcion}",
            operationId = "CatAsentamiento.findById")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Asentamiento - Obtener por c\u00f3digo")
    @Get(value = "/obtener/codigo/{codigo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByCodigoAndEsActivo(@NotBlank @PathVariable String codigo) {
        try {
            return HttpResponse.ok(catAsentamientoService.findByCodigoAndEsActivo(codigo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.CatAsentamiento.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.CatAsentamiento.findByEsActivo.descripcion}",
            operationId = "CatAsentamiento.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Asentamiento - Obtener por activo/inactivo.")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@NotBlank @PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(catAsentamientoService.findByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.CatAsentamiento.guardaAsentamiento.resumen}",
            description = "${cosmonaut.controller.CatAsentamiento.guardaAsentamiento.descripcion}",
            operationId = "CatAsentamiento.guardaAsentamiento")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Asentamiento - guardar")
    @Put(value = "/guardar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> guardar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body CatAsentamiento asentamiento) {
        try {
            return HttpResponse.ok(catAsentamientoService.guardar(asentamiento));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
     @Operation(summary = "${cosmonaut.controller.CatAsentamiento.modificaAsentamiento.resumen}",
            description = "${cosmonaut.controller.CatAsentamiento.modificaAsentamiento.descripcion}",
             operationId = "CatAsentamiento.modificaAsentamiento")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Asentamiento - modificar")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body CatAsentamiento asentamiento) {
        try {
            return HttpResponse.ok(catAsentamientoService.modificar(asentamiento));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
