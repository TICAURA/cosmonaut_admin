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
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTipoDeduccion;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsTipoDeduccionRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CsTipoDeduccionTest {
    private static final Logger LOG = LoggerFactory.getLogger(CsTipoDeduccionTest.class);

    @Inject
    @Client("/adminCatalogo/csTipoDeduccion")
    public RxHttpClient cliente;
    
    @Inject
    protected CsTipoDeduccionRepository  csTipoDeduccionRepository;

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
          CsTipoDeduccion csTipoDeduccion = new CsTipoDeduccion();
         csTipoDeduccion.setTipoDeduccionId("999");
         csTipoDeduccion.setEsActivo(true);
         csTipoDeduccion.setDescripcion("DeduccionTest");
         csTipoDeduccion.setFechaInicio(Utilidades.obtenerFechaSystema());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", csTipoDeduccion),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            csTipoDeduccion.setDescripcion("TestModificar");
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", csTipoDeduccion),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                csTipoDeduccionRepository.deleteById("999");
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
