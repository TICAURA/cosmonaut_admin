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
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatTipoValorReferencia;
import mx.com.ga.cosmonaut.common.repository.catalogo.negocio.CatValorReferenciaRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CatValorReferenciaTest {

	private static final Logger LOG = LoggerFactory.getLogger(CatValorReferenciaTest.class);
        
    @Inject
    @Client("/adminCatalogo/valorReferencia")
    public RxHttpClient cliente;
    
    @Inject
    protected CatValorReferenciaRepository catValorReferenciaRepository;

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
                cliente.toBlocking().retrieve(HttpRequest.GET("/obtener/id/" + 2),
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
         CatTipoValorReferencia catTipoValorReferencia = new CatTipoValorReferencia();
         catTipoValorReferencia.setTipoValorReferenciaId(999L);
         catTipoValorReferencia.setEsActivo(true);
         catTipoValorReferencia.setDescripcion("Test");
         catTipoValorReferencia.setNombreCorto("TestCorto");
         catTipoValorReferencia.setFechaAlta(Utilidades.obtenerFechaSystema());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", catTipoValorReferencia),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            catTipoValorReferencia.setDescripcion("Modificar");
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", catTipoValorReferencia),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                catValorReferenciaRepository.deleteById(999L);
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
