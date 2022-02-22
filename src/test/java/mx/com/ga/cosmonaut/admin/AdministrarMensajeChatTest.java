package mx.com.ga.cosmonaut.admin;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmMensajeChatCentrocostos;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmUsuarios;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatTipoMensaje;
import mx.com.ga.cosmonaut.common.entity.cliente.NclCentrocCliente;
import mx.com.ga.cosmonaut.common.entity.colaborador.NcoPersona;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdministrarMensajeChatTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModuloTests.class);

    @Inject
    @Client("/administrar-mensaje-chat")
    private RxHttpClient cliente;

    @Test
    @Order(0)
    public void testGuardar() {
        AdmMensajeChatCentrocostos mensajeChatCentrocostos = new AdmMensajeChatCentrocostos();
        mensajeChatCentrocostos.setMensajeGenerico("Prueba-Test");
        mensajeChatCentrocostos.setFechaMensajeGenerico(new Date());
        mensajeChatCentrocostos.setCentrocClienteId(new NclCentrocCliente());
        mensajeChatCentrocostos.getCentrocClienteId().setCentrocClienteId(1);
        mensajeChatCentrocostos.setUsuarioId(new AdmUsuarios());
        mensajeChatCentrocostos.getUsuarioId().setUsuarioId(45);
        mensajeChatCentrocostos.setTipoMensajeId(new CatTipoMensaje());
        mensajeChatCentrocostos.getTipoMensajeId().setTipoMensajeId(1);

        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.PUT("/guardar",mensajeChatCentrocostos), RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());

        LOG.info("Respuesta {}", respuesta.getDatos());
    }

    @Test
    @Order(1)
    public void testModificar() {
        AdmMensajeChatCentrocostos mensajeChatCentrocostos = new AdmMensajeChatCentrocostos();
        mensajeChatCentrocostos.setMensajeChatCentrocostosId(5);
        mensajeChatCentrocostos.setMensajeGenerico("Prueba Test");
        mensajeChatCentrocostos.setFechaMensajeGenerico(new Date());
        mensajeChatCentrocostos.setCentrocClienteId(new NclCentrocCliente());
        mensajeChatCentrocostos.getCentrocClienteId().setCentrocClienteId(1);
        mensajeChatCentrocostos.setUsuarioId(new AdmUsuarios());
        mensajeChatCentrocostos.getUsuarioId().setUsuarioId(45);
        mensajeChatCentrocostos.setTipoMensajeId(new CatTipoMensaje());
        mensajeChatCentrocostos.getTipoMensajeId().setTipoMensajeId(1);
        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.POST("/modificar",mensajeChatCentrocostos), RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());

        LOG.info("Respuesta {}", respuesta.getDatos());
    }

    @Test
    @Order(2)
    public void testObtenerId() {
        int mensajeChatCentroClienteId = 5;
        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.GET("/obtener/id/" + mensajeChatCentroClienteId), RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());

        LOG.info("Respuesta {}", respuesta.getDatos());
    }

    @Test
    @Order(3)
    public void testListarEmpresa() {
        int idEmpresa = 1;
        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.GET("/lista/empresa/" + idEmpresa), RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());

        LOG.info("Respuesta {}", respuesta.getDatos());
    }

}
