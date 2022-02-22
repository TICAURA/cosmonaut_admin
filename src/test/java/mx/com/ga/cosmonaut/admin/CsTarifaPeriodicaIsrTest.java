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
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsPeriodicidadPago;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTarifaPeriodicaIsr;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsTarifaPeriodicaIsrRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CsTarifaPeriodicaIsrTest {
    private static final Logger LOG = LoggerFactory.getLogger(CsTarifaPeriodicaIsrTest.class);

    @Inject
    @Client("/adminCatalogo/csTarifaPeriodicaIsr")
    public RxHttpClient cliente;
    
    @Inject
    protected CsTarifaPeriodicaIsrRepository csTarifaPeriodicaIsrRepository;

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
         CsTarifaPeriodicaIsr csTarifaPeriodicaIsr = new CsTarifaPeriodicaIsr();
         csTarifaPeriodicaIsr.setTarifaPeriodicaIsrId(999);
         csTarifaPeriodicaIsr.setEsActivo(true);
         csTarifaPeriodicaIsr.setLimiteInferior(BigDecimal.ONE);
         csTarifaPeriodicaIsr.setPeriodicidadPagoId(new CsPeriodicidadPago());
         csTarifaPeriodicaIsr.getPeriodicidadPagoId().setPeriodicidadPagoId("02");
         csTarifaPeriodicaIsr.setFechaInicio(Utilidades.obtenerFechaSystema());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", csTarifaPeriodicaIsr),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            csTarifaPeriodicaIsr.setLimiteInferior(BigDecimal.ZERO);
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", csTarifaPeriodicaIsr),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                csTarifaPeriodicaIsrRepository.deleteById(999);
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
