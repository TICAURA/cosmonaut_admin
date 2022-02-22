package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.CatTipoIncidenciaService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.catalogo.negocio.CatTipoIncidenciaRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;

import javax.inject.Inject;
import javax.inject.Singleton;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatTipoIncidencia;

@Singleton
public class CatTipoIncidenciaServiceImpl implements CatTipoIncidenciaService {

    private RespuestaGenerica respuestaGenerica = new RespuestaGenerica();

    @Inject
    private CatTipoIncidenciaRepository catTipoIncidenciaRepository;

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        try {
            respuestaGenerica.setDatos(catTipoIncidenciaRepository.findByEsActivoAndEsIncidenciaOrderByDescripcion(activo));
            respuestaGenerica.setMensaje(Constantes.EXITO);
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            return respuestaGenerica;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }
    
     public RespuestaGenerica modificar(CatTipoIncidencia catTipoIncidencia )throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(catTipoIncidencia);
            if (respuesta.isResultado()) {
                respuesta.setDatos(catTipoIncidenciaRepository.update(catTipoIncidencia));
                respuesta.setMensaje(Constantes.EXITO);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificaEstados " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    
    public RespuestaGenerica guardar(CatTipoIncidencia catTipoIncidencia ) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(catTipoIncidencia);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(catTipoIncidencia);
                if (respuesta.isResultado()) {
                    catTipoIncidencia.setEsActivo(Constantes.ESTATUS_ACTIVO);
                    respuesta.setDatos(catTipoIncidenciaRepository.save(catTipoIncidencia));
                    respuesta.setMensaje(Constantes.EXITO);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                }
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " guardaEstados " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException {
        try {
            return new RespuestaGenerica(catTipoIncidenciaRepository.findByDescripcionIlike("%" + descripcion.getDescripcion() + "%"),
                    Constantes.RESULTADO_EXITO,Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " listarEstatusDescricpcion " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(CatTipoIncidencia catTipoIncidencia ) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (catTipoIncidencia.getDescripcion()== null
                    || catTipoIncidencia.getDescripcion().isEmpty()
                    || catTipoIncidencia.getEsIncidencia() == null){
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

    private RespuestaGenerica validarEstructura(CatTipoIncidencia catTipoIncidencia ) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (validaDuplicado(catTipoIncidencia.getDescripcion())) {
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

    private boolean validaDuplicado(String desc) throws ServiceException {
        try {
            return catTipoIncidenciaRepository.findByDescripcion(desc).isEmpty();
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaDuplicado " + Constantes.ERROR_EXCEPCION, e);
        }
    }
}
