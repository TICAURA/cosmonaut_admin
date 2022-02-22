package mx.com.ga.cosmonaut.admin;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.roles.GuardarRequest;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmRoles;
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
public class RolTests {

    private static final Logger LOG = LoggerFactory.getLogger(RolTests.class);

    // VARIABLES PARA TEST
    private static Integer id;

    @Inject
    @Client("/rol")
    private RxHttpClient cliente;

    @Test
    @Order(0)
    public void testGuardar() {
        GuardarRequest rol = new GuardarRequest();
        rol.setNombreRol("Prueba");
        rol.setCentrocClienteId(1);

        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.PUT("/guardar", rol), RespuestaGenerica.class);

        assertTrue(respuesta.isResultado());
        assertEquals(Constantes.EXITO, respuesta.getMensaje());

        LOG.info("Respuesta {}", respuesta.getDatos());

        HashMap<String, Object> datos = (HashMap<String, Object>) respuesta.getDatos();

        LOG.info("Id {}", datos.get("rolId"));

        this.id = (Integer) datos.get("rolId");
    }

    @Test
    @Order(1)
    public void testModificar() {
        AdmRoles rol = new AdmRoles();
        rol.setRolId(this.id);
        rol.setNombreRol("PruebaMod");
        rol.setEsActivo(true);
        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.POST("/modificar", rol), RespuestaGenerica.class);

        assertTrue(respuesta.isResultado());
        assertEquals(Constantes.EXITO, respuesta.getMensaje());

        LOG.info("Respuesta {}", respuesta.getDatos());
    }

    @Test
    @Order(2)
    public void testObtenerId() {
        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.GET("/obtener/id/" + this.id), RespuestaGenerica.class);

        assertTrue(respuesta.isResultado());
        assertEquals(Constantes.EXITO, respuesta.getMensaje());

        LOG.info("Respuesta {}", respuesta.getDatos());
    }

    @Test
    @Order(3)
    public void testFindByEsActivo() {
        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.GET("/listar/todosActivos/" + Boolean.TRUE), RespuestaGenerica.class);

        assertTrue(respuesta.isResultado());
        assertEquals(Constantes.EXITO, respuesta.getMensaje());

        LOG.info("Respuesta {}", respuesta.getDatos());
    }
}
