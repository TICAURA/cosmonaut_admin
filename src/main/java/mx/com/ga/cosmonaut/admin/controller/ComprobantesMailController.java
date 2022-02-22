package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import mx.com.ga.cosmonaut.admin.services.ComprobanteMailService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;

@Controller("/comprobantes")
public class ComprobantesMailController {

    @Inject
    private ComprobanteMailService comprobanteMailService;
    @Operation(summary = "${cosmonaut.controller.sendMail.nominaXperiodoId.resumen}",
            description = "${cosmonaut.controller.sendMail.nominaXperiodoId.descripcion}",
            operationId = "sendMail.nominaXperiodoId")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Get(value = "/sendMail/{nominaXperiodoId}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> sendMailComprobantes(@PathVariable Integer nominaXperiodoId) {
        try {
            return HttpResponse.ok(comprobanteMailService.sendMailComprobantes(nominaXperiodoId));
        }catch (Exception e){
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
