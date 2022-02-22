package mx.com.ga.cosmonaut.admin.services.impl;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import mx.com.ga.cosmonaut.admin.services.CatAsentamientoService;
import mx.com.ga.cosmonaut.common.entity.catalogo.ubicacion.CatAsentamiento;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.repository.catalogo.ubicacion.CatAsentamientoRepository;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.util.Constantes;

@Singleton
public class CatAsentamientoServiceImpl implements CatAsentamientoService {

    @Inject
    private CatAsentamientoRepository catAsentaminetoRepository;

    public RespuestaGenerica findByCodigoAndEsActivo(String codigo) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            List<CatAsentamiento> lista = catAsentaminetoRepository.obtieneAsentamientos(codigo);
            if (!lista.isEmpty()) {
                respuesta.setDatos(lista);
                respuesta.setResultado(true);
                respuesta.setMensaje(Constantes.EXITO);
            }
            return respuesta;
        } catch (Exception e) {

            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findById " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(catAsentaminetoRepository.findByEsActivo(activo));
            respuesta.setResultado(true);
            respuesta.setMensaje(Constantes.EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    public RespuestaGenerica modificar(CatAsentamiento asentamiento) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(asentamiento);
            if (respuesta.isResultado()) {
                respuesta.setDatos(catAsentaminetoRepository.update(asentamiento));
                respuesta.setMensaje(Constantes.EXITO);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificaAsentamiento " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    public RespuestaGenerica guardar(CatAsentamiento asentamiento) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(asentamiento);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(asentamiento);
                if (respuesta.isResultado()) {
                    asentamiento.setEsActivo(Constantes.ESTATUS_ACTIVO);
                    respuesta.setDatos(catAsentaminetoRepository.save(asentamiento));
                    respuesta.setMensaje(Constantes.EXITO);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                }
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " guardaAsentamiento " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(CatAsentamiento asentamiento) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (asentamiento.getCodigo() == null
                    || asentamiento.getCodigo().isEmpty()
                    || asentamiento.getCatmnpio() == null
                    || asentamiento.getCatmnpio().getCmnpio() == null
                    || asentamiento.getAsentamientoCpCons() == null
                    || asentamiento.getAsentamiento() == null
                    || asentamiento.getAsentamiento().isEmpty()
                    || asentamiento.getTipoAsentamiento() == null
                    || asentamiento.getZona() == null
                    || asentamiento.getEdo() == null
                    || asentamiento.getEdo().getEstadoId()== null) {
                respuesta.setMensaje(Constantes.CAMPOS_REQUERIDOS);
                respuesta.setResultado(Constantes.RESULTADO_ERROR);

            } else {
                respuesta.setMensaje(Constantes.EXITO);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validarCamposObligatorios " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarEstructura(CatAsentamiento asentamiento) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (validaDuplicado(asentamiento.getCodigo(), asentamiento.getAsentamientoCpCons())) {
                respuesta.setMensaje(Constantes.EXITO);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
            } else {
                respuesta.setMensaje(Constantes.REGISTRO_EXISTE);
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validarEstructura " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private boolean validaDuplicado(String codigo, Integer idAsenta) throws ServiceException {
        try {
            return catAsentaminetoRepository.findByCodigoAndAsentamientoCpCons(codigo, idAsenta).isEmpty();
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaDuplicado " + Constantes.ERROR_EXCEPCION, e);
        }
    }
}
