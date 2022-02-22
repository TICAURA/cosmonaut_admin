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
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsRegimenFiscal;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsRegimenFiscalRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CsRegimenFiscalTest {

	private static final Logger LOG = LoggerFactory.getLogger(CsRegimenFiscalTest.class);
        
    @Inject
    @Client("/adminCatalogo/csRegimenFiscal")
    public RxHttpClient cliente;
    
    @Inject
    protected CsRegimenFiscalRepository csRegimenFiscalRepository;

    @Test
    public void testListarTodos() {
        final RespuestaGenerica respuesta =
                cliente.toBlocking().retrieve(HttpRequest.GET("/listar/todos"),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
    
    
    @Test
     public void testListarID() {
        final RespuestaGenerica respuesta =
                cliente.toBlocking().retrieve(HttpRequest.GET("/obtener/id/" + 1),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
     
     public void guardarModificarTest() {
         CsRegimenFiscal csRegimenFiscal = new CsRegimenFiscal();
         csRegimenFiscal.setRegimenfiscalId("999");
         csRegimenFiscal.setActivo(true);
         csRegimenFiscal.setDescripcion("Test");
         csRegimenFiscal.setIndPersonaMoral(false);
         csRegimenFiscal.setFecInicio(Utilidades.obtenerFechaSystema());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", csRegimenFiscal),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            csRegimenFiscal.setDescripcion("Modificar");
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", csRegimenFiscal),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                csRegimenFiscalRepository.deleteById("999");
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }


}
