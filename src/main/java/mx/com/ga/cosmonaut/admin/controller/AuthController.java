package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.admin.services.AuthService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import javax.validation.Valid;
import java.security.Principal;

@Controller("/auth")
public class AuthController {

    @Inject
    private AuthService authService;

    @Operation(summary = "${cosmonaut.controller.auth.logout.resumen}",
            description = "${cosmonaut.controller.auth.logout.descripcion}",
            operationId = "auth.logout")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Auth - Logout.")
    @Get(value = "/logout", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> logout(Principal principal) {
        try {
            return HttpResponse.ok(authService.logout(principal.getName()));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

}
