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
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsActividadEconomica;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsActividadEconomicaRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CsActividadEconomicaTest {
    private static final Logger LOG = LoggerFactory.getLogger(CsActividadEconomicaTest.class);

    @Inject
    @Client("/adminCatalogo/csActividadEconomica")
    private RxHttpClient cliente;
    
    @Inject
    protected CsActividadEconomicaRepository csActividadEconomicaRepository;

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
    
     public void guardarModificarTest() {
         CsActividadEconomica csActividadEconomica = new CsActividadEconomica();
         csActividadEconomica.setActividadEconomicaId(999);
         csActividadEconomica.setEsActivo(true);
         csActividadEconomica.setDescripcion("Test");
         csActividadEconomica.setNivel(10);
         csActividadEconomica.setFecInicio(Utilidades.obtenerFechaSystema());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", csActividadEconomica),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            csActividadEconomica.setDescripcion("Modificar");
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", csActividadEconomica),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                csActividadEconomicaRepository.deleteById(999);
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
