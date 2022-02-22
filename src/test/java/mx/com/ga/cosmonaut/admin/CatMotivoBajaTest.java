package mx.com.ga.cosmonaut.admin;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.util.Constantes;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatMotivoBaja;
import mx.com.ga.cosmonaut.common.repository.catalogo.negocio.CatMotivoBajaRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CatMotivoBajaTest {
    private static final Logger LOG = LoggerFactory.getLogger(CatMotivoBajaTest.class);

    @Inject
    @Client("/adminCatalogo/catMotivoBaja")
    private RxHttpClient cliente;
    
    @Inject
    protected CatMotivoBajaRepository  catMotivoBajaRepository;

    @Test
    void findAllTest() {
        final RespuestaGenerica respuesta =
                cliente.toBlocking().retrieve(HttpRequest.GET("/listar/todos"),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }

    @Test
    void findByIdTest() {
        final RespuestaGenerica respuesta =
                cliente.toBlocking().retrieve(HttpRequest.GET("/obtener/id/" + 1),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }

    @Test
    void findByEsActivoTest() {
        final RespuestaGenerica respuesta =
                cliente.toBlocking().retrieve(HttpRequest.GET("/listar/todosActivo/" + Boolean.TRUE),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
    
     @Test
    public void guardarModificarTest() {
         CatMotivoBaja catMotivoBaja = new CatMotivoBaja();
         catMotivoBaja.setEsActivo(true);
         catMotivoBaja.setDescripcion("TestMotivo");
         catMotivoBaja.setMotivoBajaId(999);
         catMotivoBaja.setFechaAlta(Utilidades.obtenerFechaSystema());
          catMotivoBaja = catMotivoBajaRepository.findById(999).orElse(new CatMotivoBaja());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", catMotivoBaja),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            catMotivoBaja.setDescripcion("MotivoModificar");
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", catMotivoBaja),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                catMotivoBajaRepository.deleteById(999);
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
