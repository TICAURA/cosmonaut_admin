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
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatTipoIncapacidad;
import mx.com.ga.cosmonaut.common.repository.catalogo.negocio.CatTipoIncapacidadRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CatTipoIncapacidadTest {
    private static final Logger LOG = LoggerFactory.getLogger(CatTipoIncapacidadTest.class);

    @Inject
    @Client("/adminCatalogo/tipoIncapacidad")
    public RxHttpClient cliente;
    
    @Inject
    protected CatTipoIncapacidadRepository  catTipoIncapacidadRepository;

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
         CatTipoIncapacidad catTipoIncapacidad = new CatTipoIncapacidad();
         catTipoIncapacidad.setDescripcion("Test");
         catTipoIncapacidad.setEsIncidencia(true);
         catTipoIncapacidad.setNombreCorto("TestCorto");
         catTipoIncapacidad.setTipoIncapacidadId(99);
         catTipoIncapacidad.setFechaAlta(Utilidades.obtenerFechaSystema());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", catTipoIncapacidad),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            catTipoIncapacidad.setDescripcion("TestModificar");
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", catTipoIncapacidad),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                catTipoIncapacidadRepository.deleteById(99);
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
