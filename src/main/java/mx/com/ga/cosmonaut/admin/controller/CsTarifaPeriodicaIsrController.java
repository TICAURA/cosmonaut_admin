package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.admin.services.CsTarifaPeriodicaIsrService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTarifaPeriodicaIsr;

import java.util.List;
import java.util.Set;

@Controller("/adminCatalogo/csTarifaPeriodicaIsr")
public class CsTarifaPeriodicaIsrController {

    @Inject
    private CsTarifaPeriodicaIsrService csTarifaPeriodicaIsrService;

    @Operation(summary = "${cosmonaut.controller.csTarifaPeriodicaIsr.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.csTarifaPeriodicaIsr.findByEsActivo.descripcion}",
            operationId = "csTarifaPeriodicaIsr.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tarifa Periodica ISR - Listar Todos activo/inactivo.")
    @Get(value = "/listar/todosActivo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(csTarifaPeriodicaIsrService.findByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.csTarifaPeriodicaIsr.guardar.resumen}",
            description = "${cosmonaut.controller.csTarifaPeriodicaIsr.guardar.descripcion}",
            operationId = "csTarifaPeriodicaIsr.guardar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tarifa Periodica ISR  - guardar")
    @Put(value = "/guardar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> guardar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body CsTarifaPeriodicaIsr csTarifaPeriodicaIsr) {
        try {
            return HttpResponse.ok(csTarifaPeriodicaIsrService.guardar(csTarifaPeriodicaIsr));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.csTarifaPeriodicaIsr.modificar.resumen}",
            description = "${cosmonaut.controller.csTarifaPeriodicaIsr.modificar.descripcion}",
            operationId = "csTarifaPeriodicaIsr.modificar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tarifa Periodica ISR  - modificar")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body CsTarifaPeriodicaIsr csTarifaPeriodicaIsr) {
        try {
            return HttpResponse.ok(csTarifaPeriodicaIsrService.modificar(csTarifaPeriodicaIsr));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.csTarifaPeriodicaIsr.modificarMultiple.resumen}",
            description = "${cosmonaut.controller.csTarifaPeriodicaIsr.modificarMultiple.descripcion}",
            operationId = "csTarifaPeriodicaIsr.modificarMultiple")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Tarifa Periodica ISR  - modificar multiples registros")
    @Post(value = "/modificar/multiple", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificarMultiple(@Header("datos-flujo") String datosFlujo,
                                                             @Header("datos-sesion") String datosSesion,
                                                             @Body List<CsTarifaPeriodicaIsr> valoresTablaPeriodicaISR) {
        try {
            return HttpResponse.ok(csTarifaPeriodicaIsrService.modificarMultiple(valoresTablaPeriodicaISR));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
