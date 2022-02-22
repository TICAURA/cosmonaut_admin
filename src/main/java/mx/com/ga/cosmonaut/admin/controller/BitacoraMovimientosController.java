package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.consultas.BitacoraCosmonautConsulta;
import mx.com.ga.cosmonaut.common.service.AdmBitacoraCosmonautService;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;

@Controller("/bitacora-movimientos")
public class BitacoraMovimientosController {

    @Inject
    private AdmBitacoraCosmonautService bitacoraCosmonautService;

    @Operation(summary = "${cosmonaut.controller.bitacoramovimientos.listar.resumen}",
            description = "${cosmonaut.controller.bitacoramovimientos.listar.descripcion}",
            operationId = "bitacoramovimientos.listar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Bitacora Movimientos - Listar ")
    @Post(value = "/listar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listar(@Body BitacoraCosmonautConsulta bitacora) {
        try {
            return HttpResponse.ok(bitacoraCosmonautService.listar(bitacora));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
