package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.inject.Inject;
import mx.com.ga.cosmonaut.admin.services.TablasValoresService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.util.Utilidades;

@Controller("/TablasValores")
public class TablasValoresController {

    @Inject
    private TablasValoresService tablasValoresService;


    @Operation(summary = "${cosmonaut.controller.tablaValorReferencia.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.tablaValorReferencia.findByEsActivo.descripcion}",
            operationId = "tablaValorReferencia.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Tabla Valor Referencia - Listar Todos activo/inactivo.")
    @Get(value = "/listar/valorReferencia/{activo}/{anio}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findValorReferenciaByEsActivo(@PathVariable Boolean activo, Long anio) {
        try {
            return HttpResponse.ok(tablasValoresService.findValorReferenciaByEsActivo(activo, anio));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.tablaTarifaIsr.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.tablaTarifaIsr.findByEsActivo.descripcion}",
            operationId = "tablaTarifaIsr.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Tabla Tarífa Periódica ISR - Listar Todos activo/inactivo.")
    @Get(value = "/listar/tarifaISR/{activo}/{periodicidad}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findTarifaIsrByEsActivo(@PathVariable Boolean activo, String periodicidad) {
        try {
            return HttpResponse.ok(tablasValoresService.findTarifaIsrByEsActivo(activo, periodicidad));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }
    
     @Operation(summary = "${cosmonaut.controller.tablaSubsidioIsr.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.tablaSubsidioIsr.findByEsActivo.descripcion}",
             operationId = "tablaSubsidioIsr.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Tabla Tarífa Subsidio ISR - Listar Todos activo/inactivo.")
    @Get(value = "/listar/tablaSubsidioIsr/{activo}/{periodicidad}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findTarifaSubsidioIsrByEsActivo(@PathVariable Boolean activo, String periodicidad) {
        try {
            return HttpResponse.ok(tablasValoresService.findTarifaSubsidioIsrByEsActivo(activo, periodicidad));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }
    
     @Operation(summary = "${cosmonaut.controller.tasaAplicableIsn.findTablasISRPeriodicas.resumen}",
            description = "${cosmonaut.controller.tasaAplicableIsn.findTablasISRPeriodicas.descripcion}",
             operationId = "tasaAplicableIsn.findTablasISRPeriodicas")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Lista Tablas ISR - Listar Todos.")
    @Get(value = "/listar/tablasPeriodicasISR", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findTablasISRPeriodicas() {
        try {
            return HttpResponse.ok(tablasValoresService.findTablasISRPeriodicas());
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }
    
    @Operation(summary = "${cosmonaut.controller.tasaAplicableIsn.findTablasISRSubsidio.resumen}",
            description = "${cosmonaut.controller.tasaAplicableIsn.findTablasISRSubsidio.descripcion}",
            operationId = "tasaAplicableIsn.findTablasISRSubsidio")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Lista Tablas Subsidio ISR - Listar Todos.")
    @Get(value = "/listar/tablasSubsidioISR", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findTablasISRSubsidio() {
        try {
            return HttpResponse.ok(tablasValoresService.findTablasISRSubsidio());
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.tasaAplicableIsn.findTablasISN.resumen}",
            description = "${cosmonaut.controller.tasaAplicableIsn.findTablasISN.descripcion}",
            operationId = "tasaAplicableIsn.findTablasISN")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Lista Tablas ISN - Listar Todos.")
    @Get(value = "/listar/obtieneTablasISN", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findTablasISN() {
        try {
            return HttpResponse.ok(tablasValoresService.findTablasISN());
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.findEstadosISN.findTablasISN.resumen}",
            description = "${cosmonaut.controller.findEstadosISN.findTablasISN.descripcion}",
            operationId = "findEstadosISN.findTablasISN")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Lista Estados ISN - Lista los estados que tienen un ISN adjuntado.")
    @Get(value = "/listar/estadosISN", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findEstadosISN() {
        try {
            return HttpResponse.ok(tablasValoresService.findEstadosISN());
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.tasaAplicableIsn.findByEsActivo.resumen}",
            description = "${cosmonaut.controller.tasaAplicableIsn.findByEsActivo.descripcion}",
            operationId = "tasaAplicableIsn.findByEsActivo")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Tabla Tasa aplicable ISN - Listar Todos activo/inactivo.")
    @Get(value = "/listar/tasaAplicableISN/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findTasaAplicableIsnByEsActivo(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(tablasValoresService.findTasaAplicableIsnByEsActivo(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.tasaAplicableIsn.findTasaAplicableIsnByEsActivoAndCestadoEstadoId.resumen}",
            description = "${cosmonaut.controller.tasaAplicableIsn.findTasaAplicableIsnByEsActivoAndCestadoEstadoId.descripcion}",
            operationId = "tasaAplicableIsn.findTasaAplicableIsnByEsActivoAndCestadoEstadoId")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Tabla Tasa aplicable ISN por Estado - Listar Todos por Estado y activo/inactivo.")
    @Get(value = "/listar/tasaAplicableISN/{activo}/{estadoId}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> findByEsActivoAndCestadoEstadoId(@PathVariable Boolean activo, @PathVariable Integer estadoId) {
        try {
            return HttpResponse.ok(tablasValoresService.findByEsActivoAndCestadoEstadoId(activo, estadoId));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.tablaValorReferencia.listarvalorreferenciatodos.resumen}",
            description = "${cosmonaut.controller.tablaValorReferencia.listarvalorreferenciatodos.descripcion}",
            operationId = "tablaValorReferencia.listarvalorreferenciatodos")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Tabla Valor Referencia - Listar Todos activo/inactivo.")
    @Get(value = "/listar/valorReferencia/", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listarValorReferenciaTodos() {
        try {
            return HttpResponse.ok(tablasValoresService.listarValorReferenciaTodos());
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.tablaValorReferencia.listarestatusdescricpcion.resumen}",
            description = "${cosmonaut.controller.tablaValorReferencia.listarestatusdescricpcion.descripcion}",
            operationId = "csBanco.listarestatusdescricpcion")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Tabla Valor Referencia - Listar todos activo/inactivo.")
    @Post(value = "/listar/descripcion", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listarEstatusDescricpcion(@Body DescripcionRequest descripcion) {
        try {
            return HttpResponse.ok(tablasValoresService.listarEstatusDescripcion(descripcion));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }
    
}
