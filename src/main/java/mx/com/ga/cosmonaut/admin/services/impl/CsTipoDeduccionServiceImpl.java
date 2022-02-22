package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.CsTipoDeduccionService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsTipoDeduccionRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;

import javax.inject.Inject;
import javax.inject.Singleton;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTipoDeduccion;

@Singleton
public class CsTipoDeduccionServiceImpl implements CsTipoDeduccionService{

    @Inject
    private CsTipoDeduccionRepository csTipoDeduccionRepository;

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(csTipoDeduccionRepository.findByEsActivoNoConcat(activo));
            respuesta.setMensaje(Constantes.EXITO);
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }
    
      public RespuestaGenerica modificar(CsTipoDeduccion csTipoDeduccion) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csTipoDeduccion);
            if (respuesta.isResultado()) {
                respuesta.setDatos(csTipoDeduccionRepository.update(csTipoDeduccion));
                respuesta.setMensaje(Constantes.EXITO);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificaEstados " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    
    public RespuestaGenerica guardar(CsTipoDeduccion csTipoDeduccion) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csTipoDeduccion);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(csTipoDeduccion);
                if (respuesta.isResultado()) {
                    csTipoDeduccion.setEsActivo(Constantes.ESTATUS_ACTIVO);
                    respuesta.setDatos(csTipoDeduccionRepository.save(csTipoDeduccion));
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
            return new RespuestaGenerica(csTipoDeduccionRepository.findByDescripcionIlike("%" + descripcion.getDescripcion() + "%"),
                    Constantes.RESULTADO_EXITO,Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " listarEstatusDescricpcion " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(CsTipoDeduccion csTipoDeduccion) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (csTipoDeduccion.getDescripcion()== null
                    || csTipoDeduccion.getDescripcion().isEmpty()) {
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

    private RespuestaGenerica validarEstructura(CsTipoDeduccion csTipoDeduccion) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (validaDuplicado(csTipoDeduccion.getDescripcion(), csTipoDeduccion.getTipoDeduccionId())) {
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

    private boolean validaDuplicado(String desc, String id) throws ServiceException {
        try {
            return csTipoDeduccionRepository.findByDescripcion(desc).isEmpty()
                    && csTipoDeduccionRepository.findByTipoDeduccionId(id).isEmpty();
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaDuplicado " + Constantes.ERROR_EXCEPCION, e);
        }
    }
}
