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
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatMetodoPago;
import mx.com.ga.cosmonaut.common.repository.catalogo.negocio.CatMetodoPagoRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CatMetodoPagoTest {
    private static final Logger LOG = LoggerFactory.getLogger(CatMetodoPagoTest.class);

    @Inject
    @Client("/adminCatalogo/catMetodoPago")
    private RxHttpClient cliente;
    
    @Inject
    protected CatMetodoPagoRepository  catMetodoPagoRepository;

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
         CatMetodoPago catMetodoPago = new CatMetodoPago();
         catMetodoPago.setEsActivo(true);
         catMetodoPago.setDescripcion("TestMetodo");
         catMetodoPago.setMetodoPagoId(99999);
         catMetodoPago.setFechaAlta(Utilidades.obtenerFechaSystema());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", catMetodoPago),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            catMetodoPago.setMetodoPagoId(99998);
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", catMetodoPago),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                catMetodoPagoRepository.deleteById(99998);
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
