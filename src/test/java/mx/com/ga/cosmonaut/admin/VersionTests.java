package mx.com.ga.cosmonaut.admin;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.version.AsignarQuitarRequest;
import mx.com.ga.cosmonaut.common.dto.administracion.version.ModificarRequest;
import mx.com.ga.cosmonaut.common.util.Constantes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VersionTests {

    private static final Logger LOG = LoggerFactory.getLogger(VersionTests.class);

    // VARIABLES PARA TEST
    private static final Integer idCliente = 1;
    private static final Integer idVersion = 1;
    private static Integer id = 4;

    @Inject
    @Client("/version")
    private RxHttpClient cliente;

    @Test
    @Order(0)
    public void testAsignar() {
        try {
            AsignarQuitarRequest asignar = new AsignarQuitarRequest();
            asignar.setCentrocClienteId(1);
            asignar.setVersionId(1);

            final RespuestaGenerica respuesta = cliente.toBlocking()
                    .retrieve(HttpRequest.PUT("/asignar", asignar), RespuestaGenerica.class);

            assertTrue(respuesta.isResultado());
            assertEquals(Constantes.EXITO, respuesta.getMensaje());

            LOG.info("Respuesta {}", respuesta.getDatos());

            HashMap<String, Object> datos = (HashMap<String, Object>) respuesta.getDatos();

            LOG.info("Id {}", datos.get("versionCosmonautXclienteId"));

            this.id = (Integer) datos.get("versionCosmonautXclienteId");
        } catch (HttpClientResponseException ex) {
            System.out.println("****ERROR -> " +ex.getMessage());
            assertTrue(ex.getMessage().equals("Bad Request"));
        }
    }

    @Test
    @Order(1)
    public void testFindByEsActivo() {
        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.GET("/listar/todosActivos/" + Boolean.TRUE), RespuestaGenerica.class);

        assertTrue(respuesta.isResultado());
        assertEquals(Constantes.EXITO, respuesta.getMensaje());

        LOG.info("Respuesta {}", respuesta.getDatos());
    }

    @Test
    @Order(2)
    public void testFindSubmodulosByVersionIdAndEsActivo() {
        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.GET("/" +idVersion+ "/listar/submodulos/todosActivos/" + Boolean.TRUE), RespuestaGenerica.class);

        assertTrue(respuesta.isResultado());
        assertEquals(Constantes.EXITO, respuesta.getMensaje());

        LOG.info("Respuesta {}", respuesta.getDatos());
    }

    @Test
    @Order(3)
    public void testModificar() {
        ModificarRequest modificar = new ModificarRequest();
        modificar.setVersionCosmonautXclienteId(id);
        modificar.setCentrocClienteId(1);
        modificar.setVersionId(1);

        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.POST("/modificar", modificar), RespuestaGenerica.class);

        assertTrue(respuesta.isResultado());
        assertEquals(Constantes.EXITO, respuesta.getMensaje());

        LOG.info("Respuesta {}", respuesta.getDatos());

        HashMap<String, Object> datos = (HashMap<String, Object>) respuesta.getDatos();

        LOG.info("Id {}", datos.get("versionCosmonautXclienteId"));

        this.id = (Integer) datos.get("versionCosmonautXclienteId");
    }

    @Test
    @Order(4)
    public void testFindVersionByClienteId() {
        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.GET("/cliente/" + idCliente), RespuestaGenerica.class);

        assertTrue(respuesta.isResultado());
        assertEquals(Constantes.EXITO, respuesta.getMensaje());

        LOG.info("Respuesta {}", respuesta.getDatos());
    }

}
