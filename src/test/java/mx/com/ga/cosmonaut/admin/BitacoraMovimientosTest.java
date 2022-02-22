package mx.com.ga.cosmonaut.admin;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.consultas.BitacoraCosmonautConsulta;
import mx.com.ga.cosmonaut.common.util.Constantes;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class BitacoraMovimientosTest {

    private static final Logger LOG = LoggerFactory.getLogger(BitacoraMovimientosTest.class);

    @Inject
    @Client("/bitacora-movimientos")
    private RxHttpClient cliente;

    @Test
    public void testListar() {
        BitacoraCosmonautConsulta bitacora = new BitacoraCosmonautConsulta();
        bitacora.setCentroClienteId(572);
        final RespuestaGenerica respuesta = cliente.toBlocking()
                .retrieve(HttpRequest.POST("/listar", bitacora), RespuestaGenerica.class);
        LOG.info("Respuesta {}", respuesta.getDatos());
        assertTrue(respuesta.isResultado());
        assertEquals(Constantes.EXITO, respuesta.getMensaje());
    }
}
