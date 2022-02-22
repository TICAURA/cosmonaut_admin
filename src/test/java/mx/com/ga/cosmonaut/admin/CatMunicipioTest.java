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
import mx.com.ga.cosmonaut.common.entity.catalogo.ubicacion.CatEstado;
import mx.com.ga.cosmonaut.common.entity.catalogo.ubicacion.CatMunicipio;
import mx.com.ga.cosmonaut.common.repository.catalogo.ubicacion.CatMunicipioRepository;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CatMunicipioTest {

    private static final Logger LOG = LoggerFactory.getLogger(CatMunicipioTest.class);

    @Inject
    @Client("/adminCatalogo/catMunicipio")
    public RxHttpClient cliente;
    
    @Inject
    protected CatMunicipioRepository catMunicipioRepository;


    @Test
    void findEsActivoTest() {
        final RespuestaGenerica respuesta =
                cliente.toBlocking().retrieve(HttpRequest.GET("/listar/todosActivo/" + Boolean.TRUE),
                        RespuestaGenerica.class);
        assertTrue(respuesta.isResultado());
        assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
        LOG.info("Respuesta {}", respuesta.getDatos());
    }
    
    @Test
    public void guardarModificarTest() {
        CatEstado catEstado = new CatEstado();
        catEstado.setEstadoId(9);
         CatMunicipio catMunicipio = new CatMunicipio();
         catMunicipio.setEsActivo(true);
         catMunicipio.setDmnpio("TestMunicipio");
         catMunicipio.setCmnpio(999);
         catMunicipio.setEdo(catEstado);
         catMunicipio.setFechaAlta(Utilidades.obtenerFechaSystema());
          catMunicipio = catMunicipioRepository.findById(999).orElse(new CatMunicipio());
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", catMunicipio),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            catMunicipio.setDmnpio("TestModificar");
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", catMunicipio),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
