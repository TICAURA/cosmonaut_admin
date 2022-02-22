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
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTipoRegimenContratacion;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsTipoRegimenContratacionRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CsTipoRegimenContratacionTest {

	private static final Logger LOG = LoggerFactory.getLogger(CsTipoRegimenContratacionTest.class);
        
    @Inject
    @Client("/adminCatalogo/csTipoRegimenContratacion")
    public RxHttpClient cliente;
    
    @Inject
    protected  CsTipoRegimenContratacionRepository csTipoRegimenContratacionRepository;

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
          CsTipoRegimenContratacion csTipoRegimenContratacion = new CsTipoRegimenContratacion();
         csTipoRegimenContratacion.setTipoRegimenContratacionId("999");
         csTipoRegimenContratacion.setEsActivo(true);
         csTipoRegimenContratacion.setDescripcion("Test");
         csTipoRegimenContratacion.setFechaInicio(Utilidades.obtenerFechaSystema());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", csTipoRegimenContratacion),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            csTipoRegimenContratacion.setDescripcion("TestModificar");
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", csTipoRegimenContratacion),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                csTipoRegimenContratacionRepository.deleteById("999");
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
