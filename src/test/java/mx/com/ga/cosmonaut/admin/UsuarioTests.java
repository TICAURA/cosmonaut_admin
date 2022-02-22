package mx.com.ga.cosmonaut.admin;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.usuarios.GuardarRequest;
import mx.com.ga.cosmonaut.common.dto.administracion.usuarios.ModificarRequest;
import mx.com.ga.cosmonaut.common.util.Constantes;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioTests {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioTests.class);

    // VARIABLES PARA TEST
    private static Integer id;

    //protected static DataSource ds;

    @Inject
    @Client("/usuarios")
    private RxHttpClient cliente;

    /*@Inject
    protected DataSource dataSource;*/

    @Test
    @Order(0)
    public void testGuardar() {
        GuardarRequest usuario = new GuardarRequest();
        usuario.setNombre("Juano");
        usuario.setApellidoPat("Sun");
        usuario.setApellidoMat("Zhian");
        usuario.setEmail("jferrer@asg.mx");
        usuario.setCentrocClienteIds(new HashSet<>(){{ add(1); }});
        usuario.setRolId(3);

        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.PUT("/guardar", usuario), RespuestaGenerica.class);

        assertTrue(respuesta.isResultado());
        assertEquals(Constantes.EXITO, respuesta.getMensaje());

        LOG.info("Respuesta {}", respuesta.getDatos());

        HashMap<String, Object> datos = (HashMap<String, Object>) respuesta.getDatos();

        LOG.info("Id {}", datos.get("usuarioId"));

        this.id = (Integer) datos.get("usuarioId");

        //ds = dataSource;
    }

    @Test
    @Order(1)
    public void testModificar() {
        ModificarRequest usuario = new ModificarRequest();
        usuario.setUsuarioId(this.id);
        usuario.setNombre("JuanoMod");
        usuario.setApellidoPat("SunMod");
        usuario.setApellidoMat("ZhianMod");
        usuario.setRolId(2);

        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.POST("/modificar", usuario), RespuestaGenerica.class);

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

    /*@AfterAll
    public static void tearDown() throws SQLException {
        ds.getConnection().prepareStatement("DELETE FROM pbg_documento WHERE id_cliente=" + idUsuario).execute();
        ds.getConnection().prepareStatement("DELETE FROM pbg_cliente WHERE id=" + idUsuario).execute();
    }*/

}
