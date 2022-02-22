package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.admin.services.CsTarifaPeriodicaSubsidioService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTarifaPeriodicaSubsidio;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import java.util.Set;

@Controller("/adminCatalogo/csTarifaPeriodicaSubsidio")
public class CsTarifaPeriodicaSubsidioController {

    @Inject
    private CsTarifaPeriodicaSubsidioService csTarifaPeriodicaSubsidioService;

    @Operation(summary = "${cosmonaut.controller.csTarifaPeriodicaSubsidio.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.csTarifaPeriodicaSubsidio.findByEsActivo.descripcion}",
            operationId = "csTarifaPeriodicaSubsidio.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tarifa Periodica Subsidio - Listar Todos activo/inactivo.")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(csTarifaPeriodicaSubsidioService.findByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
     @Operation(summary = "${cosmonaut.controller.csTarifaPeriodicaSubsidio.guardar.resumen}",
            description = "${cosmonaut.controller.csTarifaPeriodicaSubsidio.guardar.descripcion}",
             operationId = "csTarifaPeriodicaSubsidio.guardar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tarifa Periodica Subsidio  - guardar")
    @Put(value = "/guardar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> guardar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body CsTarifaPeriodicaSubsidio csTarifaPeriodicaSubsidio) {
        try {
            return HttpResponse.ok(csTarifaPeriodicaSubsidioService.guardar(csTarifaPeriodicaSubsidio));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.csTarifaPeriodicaSubsidio.modificar.resumen}",
            description = "${cosmonaut.controller.csTarifaPeriodicaSubsidio.modificar.descripcion}",
            operationId = "csTarifaPeriodicaSubsidio.modificar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tarifa Periodica Subsidio  - modificar")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body CsTarifaPeriodicaSubsidio csTarifaPeriodicaSubsidio) {
        try {
            return HttpResponse.ok(csTarifaPeriodicaSubsidioService.modificar(csTarifaPeriodicaSubsidio));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.csTarifaPeriodicaSubsidio.modificarMultiple.resumen}",
            description = "${cosmonaut.controller.csTarifaPeriodicaSubsidio.modificarMultiple.descripcion}",
            operationId = "csTarifaPeriodicaSubsidio.modificarMultiple")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tarifa Periodica Subsidio  - modificar multiples registros")
    @Post(value = "/modificar/multiple", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificarMultiple(@Header("datos-flujo") String datosFlujo,
                                                             @Header("datos-sesion") String datosSesion,
                                                             @Body Set<CsTarifaPeriodicaSubsidio> valoresTablaPeriodicaSubsidio) {
        try {
            return HttpResponse.ok(csTarifaPeriodicaSubsidioService.modificarMultiple(valoresTablaPeriodicaSubsidio));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
