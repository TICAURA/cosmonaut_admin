package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.admin.services.CatTasaAplicableIsnService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatTasaAplicableIsn;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import java.util.List;

@Controller("/adminCatalogo/catTasaAplicableIsn")
public class CatTasaAplicableIsnController {

    @Inject
    private CatTasaAplicableIsnService catTasaAplicableIsnService;

    @Operation(summary = "${cosmonaut.controller.catTasaAplicableIsn.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.catTasaAplicableIsn.findByEsActivo.descripcion}",
            operationId = "catTasaAplicableIsn.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tasa Aplicable ISN - Obtener activo/inactivo.")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(catTasaAplicableIsnService.findByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.catTasaAplicableIsn.guardar.resumen}",
            description = "${cosmonaut.controller.catTasaAplicableIsn.guardar.descripcion}",
            operationId = "catTasaAplicableIsn.guardar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tasa Aplicable ISN - guardar")
    @Put(value = "/guardar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> guardar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body CatTasaAplicableIsn tasaAplicableIsn) {
        try {
            return HttpResponse.ok(catTasaAplicableIsnService.guardar(tasaAplicableIsn));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.catTasaAplicableIsn.modificar.resumen}",
            description = "${cosmonaut.controller.catTasaAplicableIsn.modificar.descripcion}",
            operationId = "catTasaAplicableIsn.modificar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tasa Aplicable ISN - modificar")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body CatTasaAplicableIsn tasaAplicableIsn) {
        try {
            return HttpResponse.ok(catTasaAplicableIsnService.modificar(tasaAplicableIsn));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.catTasaAplicableIsn.modificarMultiple.resumen}",
            description = "${cosmonaut.controller.catTasaAplicableIsn.modificarMultiple.descripcion}",
            operationId = "catTasaAplicableIsn.modificarMultiple")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tasa Aplicable ISN - modificar multiples registros")
    @Post(value = "/modificar/multiple", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)

    public HttpResponse<RespuestaGenerica> modificarMultiple(@Header("datos-flujo") String datosFlujo,
                                                             @Header("datos-sesion") String datosSesion,
                                                             @Body List<CatTasaAplicableIsn> valoresTasaAplicableIsn) {

        try {

            return HttpResponse.ok(catTasaAplicableIsnService.modificarMultiple(valoresTasaAplicableIsn));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
