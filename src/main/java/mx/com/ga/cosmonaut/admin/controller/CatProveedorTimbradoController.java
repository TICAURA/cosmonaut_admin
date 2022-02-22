package mx.com.ga.cosmonaut.admin.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ga.cosmonaut.admin.services.CatProveedorTimbradoService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatProveedorTimbrado;
import mx.com.ga.cosmonaut.common.interceptor.BitacoraSistema;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

@Controller("/admin-catalogo/cat-proveedor-timbrado")
public class CatProveedorTimbradoController {

    @Inject
    private CatProveedorTimbradoService catProveedorTimbradoService;

    @Operation(summary = "${cosmonaut.controller.catproveedortimbrado.listar.resumen}",
            description = "${cosmonaut.controller.catproveedortimbrado.listar.descripcion}",
            operationId = "catproveedortimbrado.listar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Proveedor Dispersion - Listar Todos")
    @Get(value = "/listar/todos", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listar() {
        try {
            return HttpResponse.ok(catProveedorTimbradoService.listar());
        } catch (Exception e) {

            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.catproveedortimbrado.obtenerid.resumen}",
            description = "${cosmonaut.controller.catproveedortimbrado.obtenerid.descripcion}",
            operationId = "catproveedortimbrado.obtenerid")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Proveedor Dispersion - Obtener por ID")
    @Get(value = "/obtener/id/{id}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> obtenerId(@NotBlank @PathVariable Integer id) {
        try {
            return HttpResponse.ok(catProveedorTimbradoService.obtenerId(id));
        } catch (Exception e) {

            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.catproveedortimbrado.listaractivos.resumen}",
            description = "${cosmonaut.controller.catproveedortimbrado.listaractivos.descripcion}",
            operationId = "catproveedortimbrado.listaractivos")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Proveedor Dispersion - Listar Todos activo/inactivo")
    @Get(value = "/listar/activo/{activo}", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listarActivos(@PathVariable Boolean activo) {
        try {
            return HttpResponse.ok(catProveedorTimbradoService.listarActivos(activo));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.catproveedortimbrado.guardar.resumen}",
            description = "${cosmonaut.controller.catproveedortimbrado.guardar.descripcion}",
            operationId = "catproveedortimbrado.guardar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Proveedor Dispersion - guardar")
    @Put(value = "/guardar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> guardar(@Header("datos-flujo") String datosFlujo,
                                                   @Header("datos-sesion") String datosSesion,
                                                   @Body CatProveedorTimbrado proveedorTimbrado) {
        try {
            return HttpResponse.ok(catProveedorTimbradoService.guardar(proveedorTimbrado));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @BitacoraSistema
    @Operation(summary = "${cosmonaut.controller.catproveedortimbrado.modificar.resumen}",
            description = "${cosmonaut.controller.catproveedortimbrado.modificar.descripcion}",
            operationId = "catproveedortimbrado.modificar")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Proveedor Dispersion - modificar")
    @Post(value = "/modificar", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> modificar(@Header("datos-flujo") String datosFlujo,
                                                     @Header("datos-sesion") String datosSesion,
                                                     @Body CatProveedorTimbrado proveedorTimbrado) {
        try {
            return HttpResponse.ok(catProveedorTimbradoService.modificar(proveedorTimbrado));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }

    @Operation(summary = "${cosmonaut.controller.csBanco.listarestatusdescricpcion.resumen}",
            description = "${cosmonaut.controller.csBanco.listarestatusdescricpcion.descripcion}",
            operationId = "csBanco.listarestatusdescricpcion")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON),responseCode = "200", description = "Respuesta correcta")
    @Tag(name = "Catálogo Proveedor Dispersion - Listar todos activo/inactivo y descripcion.")
    @Post(value = "/listar/descripcion", consumes = MediaType.APPLICATION_JSON, processes = MediaType.APPLICATION_JSON)
    public HttpResponse<RespuestaGenerica> listarEstatusDescripcion(@Body DescripcionRequest descripcion) {
        try {
            return HttpResponse.ok(catProveedorTimbradoService.listarEstatusDescripcion(descripcion));
        } catch (Exception e) {
            return HttpResponse.badRequest(Utilidades.respuestaError());
        }
    }
}
