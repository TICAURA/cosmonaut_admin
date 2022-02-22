package mx.com.ga.cosmonaut.admin;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.math.BigDecimal;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.util.Constantes;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatTasaAplicableIsn;
import mx.com.ga.cosmonaut.common.entity.catalogo.ubicacion.CatEstado;
import mx.com.ga.cosmonaut.common.repository.catalogo.negocio.CatTasaAplicableIsnRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CatTasaAplicableIsnTest {
    private static final Logger LOG = LoggerFactory.getLogger(CatTasaAplicableIsnTest.class);

    @Inject
    @Client("/adminCatalogo/catTasaAplicableIsn")
    public RxHttpClient cliente;
    
    @Inject
    protected CatTasaAplicableIsnRepository  catTasaAplicableIsnRepository;

    @Test
    public void findEsActivoTest() {
        final RespuestaGenerica respuesta =
                cliente.toBlocking().retrieve(HttpRequest.GET("/listar/todosActivo/" + Boolean.TRUE),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
    
     @Test
    public void guardarModificarTest() {
         CatTasaAplicableIsn catTasaAplicableIsn = new CatTasaAplicableIsn();
         catTasaAplicableIsn.setEsActivo(true);
         catTasaAplicableIsn.setLimiteInferior(BigDecimal.valueOf(0L));
         catTasaAplicableIsn.setTasaAplicableIsnId(9999);
         catTasaAplicableIsn.setCestado(new CatEstado());
         catTasaAplicableIsn.getCestado().setEstadoId(1);
         catTasaAplicableIsn.setFechaInicio(Utilidades.obtenerFechaSystema());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", catTasaAplicableIsn),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            catTasaAplicableIsn.setTasaAplicableIsnId(9998);
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", catTasaAplicableIsn),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                catTasaAplicableIsnRepository.deleteById(9998);
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
