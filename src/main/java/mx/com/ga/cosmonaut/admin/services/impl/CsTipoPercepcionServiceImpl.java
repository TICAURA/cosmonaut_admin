package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.CsTipoPercepcionService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsTipoPercepcionRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;

import javax.inject.Inject;
import javax.inject.Singleton;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTipoPercepcion;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import java.util.HashMap;
import java.util.Optional;

@Singleton
public class CsTipoPercepcionServiceImpl implements CsTipoPercepcionService {

    @Inject
    private CsTipoPercepcionRepository csTipoPercepcionRepository;

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(csTipoPercepcionRepository.findByEsActivoNoConcat(activo));
            respuesta.setMensaje(Constantes.EXITO);
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    public RespuestaGenerica findByEsActivoPeriodicidad(Boolean activo, String tipoPeriodicidad) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(csTipoPercepcionRepository.obtieneTipoPercepcionEmpleadoNoConcat(activo, tipoPeriodicidad));
            respuesta.setMensaje(Constantes.EXITO);
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }
    
    public RespuestaGenerica modificar(CsTipoPercepcion csTipoPercepcion) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csTipoPercepcion);
            if (respuesta.isResultado()) {
                Optional<CsTipoPercepcion> optTipoPercepcion = csTipoPercepcionRepository.findByTipoPercepcionIdAndEspecializacion(csTipoPercepcion.getTipoPercepcionId(),csTipoPercepcion.getEspecializacion().trim());
                if(optTipoPercepcion.isPresent()){
                    CsTipoPercepcion csTempTipoPercepcion = optTipoPercepcion.get();
                    csTempTipoPercepcion.setFechaInicio(csTipoPercepcion.getFechaInicio());
                    csTempTipoPercepcion.setDescripcion(csTipoPercepcion.getDescripcion());
                    csTempTipoPercepcion.setEsActivo(csTipoPercepcion.isEsActivo());
                    csTempTipoPercepcion.setTipoConcepto(csTipoPercepcion.getTipoConcepto());
                    csTempTipoPercepcion.setIntegraSdi(csTipoPercepcion.getIntegraSdi());
                    csTempTipoPercepcion.setIntegraIsn(csTipoPercepcion.getIntegraIsn());
                    csTempTipoPercepcion.setIntegraIsr(csTipoPercepcion.getIntegraIsr());
                    csTempTipoPercepcion.setTipoPeriodicidad(csTipoPercepcion.getTipoPeriodicidad());

                    csTempTipoPercepcion.setTipoPercepcionId(csTipoPercepcion.getTipoPercepcionId());
                    csTipoPercepcionRepository.updateByEspecializacion(csTempTipoPercepcion.getDescripcion(),csTempTipoPercepcion.getFechaInicio(),csTempTipoPercepcion.isEsActivo(),csTempTipoPercepcion.getTipoConcepto(),csTempTipoPercepcion.getIntegraSdi(),csTempTipoPercepcion.getTipoPeriodicidad(),csTempTipoPercepcion.getIntegraIsr(),csTempTipoPercepcion.getIntegraIsn(),csTempTipoPercepcion.getTipoPago(),csTempTipoPercepcion.getPorDefecto(),csTempTipoPercepcion.getTipoPercepcionId(),csTempTipoPercepcion.getEspecializacion());
                    respuesta.setDatos(csTempTipoPercepcion);
                    respuesta.setMensaje(Constantes.EXITO);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                }else{
                    respuesta.setMensaje("No existe el tipo de perceción");
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                }

            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificaEstados " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    
    public RespuestaGenerica guardar(CsTipoPercepcion csTipoPercepcion) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csTipoPercepcion);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(csTipoPercepcion);
                if (respuesta.isResultado()) {
                    csTipoPercepcion.setFechaInicio(Utilidades.obtenerFechaSystema());
                    csTipoPercepcion.setEsActivo(Constantes.ESTATUS_ACTIVO);
                    respuesta.setDatos(csTipoPercepcionRepository.save(csTipoPercepcion));
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
            return new RespuestaGenerica(csTipoPercepcionRepository.findByDescripcionIlike("%" + descripcion.getDescripcion() + "%"),
                    Constantes.RESULTADO_EXITO,Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " listarEstatusDescricpcion " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(CsTipoPercepcion csTipoPercepcion) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (csTipoPercepcion.getDescripcion()== null
                    || csTipoPercepcion.getDescripcion().isEmpty()) {
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

    private RespuestaGenerica validarEstructura(CsTipoPercepcion csTipoPercepcion) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (validaDuplicado(csTipoPercepcion.getDescripcion(), csTipoPercepcion.getTipoPercepcionId())) {
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
            return csTipoPercepcionRepository.findByDescripcionAndIdRepetido(desc.toUpperCase().trim(), id).isEmpty();
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaDuplicado " + Constantes.ERROR_EXCEPCION, e);
        }
    }
}
