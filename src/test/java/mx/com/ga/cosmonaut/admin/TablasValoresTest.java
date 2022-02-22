package mx.com.ga.cosmonaut.admin;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import javax.inject.Inject;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.util.Constantes;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class TablasValoresTest {

    private static final Logger LOG = LoggerFactory.getLogger(TablasValoresTest.class);

    @Inject
    @Client("/TablasValores")
    public RxHttpClient cliente;

    @Test
    public void testListarTodos() {
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.GET("/listar/valorReferencia/" + true + "/2021" ),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
    
     @Test
    public void findValorReferenciaByEsActivoTest() {
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.GET("/listar/valorReferencia/" + true + "/2021" ),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
    
     @Test
    public void findTarifaIsrByEsActivoTest() {
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.GET("/listar/tarifaISR/" + true + "/02" ),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
    
    @Test
    public void findTarifaSubsidioIsrByEsActivoTest() {
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.GET("/listar/tablaSubsidioIsr/" + true + "/03" ),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
    
    @Test
    public void findTasaAplicableIsnByEsActivoTest() {
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.GET("/listar/tasaAplicableISN/" + true),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
    
     @Test
    public void findTablasISRPeriodicasTest() {
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.GET("/listar/tablasPeriodicasISR"),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
    
     @Test
    public void findTablasISRSubsidioTest() {
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.GET("/listar/tablasSubsidioISR"),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
    
     @Test
    public void findTablasISNTest() {
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.GET("/listar/obtieneTablasISN"),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }

}
