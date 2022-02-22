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
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTipoPercepcion;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsTipoPercepcionRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CsTipoPercepcionTest {
    private static final Logger LOG = LoggerFactory.getLogger(CsTipoPercepcionTest.class);

    @Inject
    @Client("/adminCatalogo/csTipoPercepcion")
    public RxHttpClient cliente;
    
     @Inject
    protected CsTipoPercepcionRepository  csTipoPercepcionRepository;

    @Test
    public void findEsActivoTest() {
        final RespuestaGenerica respuesta =
                cliente.toBlocking().retrieve(HttpRequest.GET("/listar/todosActivo/" + Boolean.TRUE),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
    
     public void guardarModificarTest() {
          CsTipoPercepcion csTipoPercepcion = new CsTipoPercepcion();
         csTipoPercepcion.setTipoPercepcionId("999");
         csTipoPercepcion.setEsActivo(true);
         csTipoPercepcion.setDescripcion("PercepcionTest");
         csTipoPercepcion.setFechaInicio(Utilidades.obtenerFechaSystema());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", csTipoPercepcion),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            csTipoPercepcion.setDescripcion("TestModificar");
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", csTipoPercepcion),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                csTipoPercepcionRepository.deleteById("999");
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
