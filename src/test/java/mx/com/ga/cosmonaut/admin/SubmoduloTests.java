package mx.com.ga.cosmonaut.admin;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.util.Constantes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubmoduloTests {

    private static final Logger LOG = LoggerFactory.getLogger(SubmoduloTests.class);

    @Inject
    @Client("/submodulos")
    private RxHttpClient cliente;

    @Test
    @Order(0)
    public void testFindByEsActivo() {
        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.GET("/listar/todosActivos/" + Boolean.TRUE), RespuestaGenerica.class);

        assertTrue(respuesta.isResultado());
        assertEquals(Constantes.EXITO, respuesta.getMensaje());

        LOG.info("Respuesta {}", respuesta.getDatos());
    }
}
