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
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatTipoIncidencia;
import mx.com.ga.cosmonaut.common.repository.catalogo.negocio.CatTipoIncidenciaRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CatTipoIncidenciaTest {
    private static final Logger LOG = LoggerFactory.getLogger(CatTipoIncidenciaTest.class);

    @Inject
    @Client("/adminCatalogo/tipoIncidencia")
    public RxHttpClient cliente;
    
    @Inject
    protected CatTipoIncidenciaRepository  catTipoIncidenciaRepository;


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
         CatTipoIncidencia catTipoIncidencia = new CatTipoIncidencia();
         catTipoIncidencia.setTipoIncidenciaId(9998);
         catTipoIncidencia.setEsActivo(true);
         catTipoIncidencia.setDescripcion("Test");
         catTipoIncidencia.setEsIncidencia(true);
         catTipoIncidencia.setNombreCorto("TestCorto");
         catTipoIncidencia.setFechaAlta(Utilidades.obtenerFechaSystema());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", catTipoIncidencia),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            catTipoIncidencia.setDescripcion("Modificar");
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", catTipoIncidencia),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                catTipoIncidenciaRepository.deleteById(9998);
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
