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
import mx.com.ga.cosmonaut.common.entity.catalogo.ubicacion.CatAsentamiento;
import mx.com.ga.cosmonaut.common.entity.catalogo.ubicacion.CatEstado;
import mx.com.ga.cosmonaut.common.entity.catalogo.ubicacion.CatMunicipio;
import mx.com.ga.cosmonaut.common.repository.catalogo.ubicacion.CatAsentamientoRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CatAsentamientoTest {

    private static final Logger LOG = LoggerFactory.getLogger(CatAsentamientoTest.class);

    @Inject
    protected CatAsentamientoRepository  catAsentamientoRepository;
    @Inject
    @Client("/adminCatalogo/catAsentamiento")
    public RxHttpClient cliente;


    @Test
    public void guardarModificarTest() {

        CatAsentamiento catAsentamiento = new CatAsentamiento();
        catAsentamiento.setCodigo("999999");
        catAsentamiento.setEsActivo(true);
        catAsentamiento.setCatmnpio(new CatMunicipio());
        catAsentamiento.getCatmnpio().setCmnpio(1);
        catAsentamiento.setAsentamientoCpCons(2);
        catAsentamiento.setTipoAsentamiento(3);
        catAsentamiento.setZona(4);
        catAsentamiento.setEdo(new CatEstado());
        catAsentamiento.getEdo().setEstadoId(2);
        final RespuestaGenerica respuesta
                = cliente.toBlocking().retrieve(HttpRequest.PUT("/guardar", catAsentamiento),
                        RespuestaGenerica.class);
        if (respuesta.isResultado()) {
            catAsentamiento.setCodigo("999998");
            final RespuestaGenerica respuestaModificar = cliente.toBlocking().retrieve(HttpRequest.POST("/modificar", catAsentamiento),
                    RespuestaGenerica.class);
            if (respuestaModificar.isResultado()) {
                catAsentamientoRepository.deleteById("999998");
                assertTrue(respuesta.isResultado());
                assertTrue(Constantes.EXITO.equals(respuesta.getMensaje()));
                LOG.info("Respuesta {}", respuesta.getDatos());

            }
        }
    }
}
