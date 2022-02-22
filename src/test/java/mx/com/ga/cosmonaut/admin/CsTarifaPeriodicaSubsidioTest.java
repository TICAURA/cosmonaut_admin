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
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTarifaPeriodicaSubsidio;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsTarifaPeriodicaSubsidioRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CsTarifaPeriodicaSubsidioTest {
    private static final Logger LOG = LoggerFactory.getLogger(CsTarifaPeriodicaSubsidioTest.class);

    @Inject
    @Client("/adminCatalogo/csTarifaPeriodicaSubsidio")
    public RxHttpClient cliente;

    @Inject
    protected CsTarifaPeriodicaSubsidioRepository csTarifaPeriodicaSubsidioRepository;
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
         CsTarifaPeriodicaSubsidio csTarifaPeriodicaSubsidio = new CsTarifaPeriodicaSubsidio();
         csTarifaPeriodicaSubsidio.setTarifaPeriodicaSubsidioId(999);
         csTarifaPeriodicaSubsidio.setEsActivo(true);
         csTarifaPeriodicaSubsidio.setLimiteInferior(BigDecimal.ONE);
         csTarifaPeriodicaSubsidio.setPeriodicidadPagoId(new CsPeriodicidadPago());
         csTarifaPeriodicaSubsidio.getPeriodicidadPagoId().setPeriodicidadPagoId("02");
         csTarifaPeriodicaSubsidio.setFechaInicio(Utilidades.obtenerFechaSystema());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", csTarifaPeriodicaSubsidio),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            csTarifaPeriodicaSubsidio.setLimiteInferior(BigDecimal.ZERO);
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", csTarifaPeriodicaSubsidio),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                csTarifaPeriodicaSubsidioRepository.deleteById(999);
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
