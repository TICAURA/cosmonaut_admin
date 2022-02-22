package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.CsTarifaPeriodicaIsrService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTarifaPeriodicaSubsidio;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsTarifaPeriodicaIsrRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;

import javax.inject.Inject;
import javax.inject.Singleton;

import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTarifaPeriodicaIsr;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class CsTarifaPeriodicaIsrServiceImpl implements CsTarifaPeriodicaIsrService {

    @Inject
    private CsTarifaPeriodicaIsrRepository csTarifaPeriodicaIsrRepository;

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            respuestaGenerica.setDatos(csTarifaPeriodicaIsrRepository.findByEsActivo(activo));
            respuestaGenerica.setMensaje(Constantes.EXITO);
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            return respuestaGenerica;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica guardar(CsTarifaPeriodicaIsr csTarifaPeriodicaIsr) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            List<CsTarifaPeriodicaIsr> listaIsr = csTarifaPeriodicaIsrRepository.
                    findByEsActivoAndPeriodicidadPagoId(true,csTarifaPeriodicaIsr.getPeriodicidadPagoId());
            respuesta = validarCamposObligatorios(csTarifaPeriodicaIsr);
            if (respuesta.isResultado()) {
                /**if (this.validaRegistro(csTarifaPeriodicaIsr.getPeriodicidadPagoId().getPeriodicidadPagoId(),
                        csTarifaPeriodicaIsr.getFechaInicio(),csTarifaPeriodicaIsr.getFechaFin(),
                        csTarifaPeriodicaIsr.getLimiteInferior().doubleValue(),csTarifaPeriodicaIsr.getLimiteSuperior().doubleValue(),
                        csTarifaPeriodicaIsr.getCuotaFija().doubleValue())){
                    csTarifaPeriodicaIsr.setEsActivo(Constantes.ESTATUS_ACTIVO);
                    respuesta.setDatos(csTarifaPeriodicaIsrRepository.save(csTarifaPeriodicaIsr));
                    respuesta.setMensaje(Constantes.EXITO);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                }else {
                    respuesta = new RespuestaGenerica(null,Constantes.RESULTADO_ERROR,Constantes.ERROR_TARIFA_PERIODICA_ISR_PERIDO_EXISTENTE);
                }*/
                listaIsr.add(csTarifaPeriodicaIsr);
                respuesta = modificarMultiple(listaIsr);
                if (respuesta.isResultado()){
                    csTarifaPeriodicaIsr.setEsActivo(Constantes.ESTATUS_ACTIVO);
                    respuesta.setDatos(csTarifaPeriodicaIsrRepository.save(csTarifaPeriodicaIsr));
                }
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " guardar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica modificar(CsTarifaPeriodicaIsr csTarifaPeriodicaIsr) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csTarifaPeriodicaIsr);
            if (respuesta.isResultado()) {
                if (this.validaRegistro(csTarifaPeriodicaIsr.getPeriodicidadPagoId().getPeriodicidadPagoId(),
                        csTarifaPeriodicaIsr.getFechaInicio(),csTarifaPeriodicaIsr.getFechaFin(),
                        csTarifaPeriodicaIsr.getLimiteInferior().doubleValue(),csTarifaPeriodicaIsr.getLimiteSuperior().doubleValue(),
                        csTarifaPeriodicaIsr.getCuotaFija().doubleValue())){
                    respuesta.setDatos(csTarifaPeriodicaIsrRepository.update(csTarifaPeriodicaIsr));
                    respuesta.setMensaje(Constantes.EXITO);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                }else {
                    respuesta = new RespuestaGenerica(null,Constantes.RESULTADO_ERROR,Constantes.ERROR_TARIFA_PERIODICA_ISR_PERIDO_EXISTENTE);
                }
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificaEstados " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica modificarMultiple(List<CsTarifaPeriodicaIsr> valoresTablaPeriodicaISR) throws ServiceException {
        try {

            RespuestaGenerica respuesta = validarCamposObligatoriosModificar(valoresTablaPeriodicaISR);
            if (!respuesta.isResultado()) {
                return respuesta;
            }

            if(!validaRegistro(valoresTablaPeriodicaISR)){
                return new RespuestaGenerica(null,Constantes.RESULTADO_ERROR,Constantes.ERROR_TARIFA_PERIODICA_GENERICO);
            }

            Set<CsTarifaPeriodicaIsr> datos = new HashSet<>();
            valoresTablaPeriodicaISR.forEach(isr -> {
                isr.setEsActivo(Constantes.ESTATUS_ACTIVO);
                datos.add(csTarifaPeriodicaIsrRepository.update(isr));
            });

            return new RespuestaGenerica(datos,Constantes.RESULTADO_EXITO,Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificarMultipleISR " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(CsTarifaPeriodicaIsr csTarifaPeriodicaIsr) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (csTarifaPeriodicaIsr.getLimiteInferior() == null
                    || csTarifaPeriodicaIsr.getCuotaFija()== null
                    || csTarifaPeriodicaIsr.getPorcExcedenteLimInf() == null
                    || csTarifaPeriodicaIsr.getPeriodicidadPagoId()== null
                    || csTarifaPeriodicaIsr.getPeriodicidadPagoId().getPeriodicidadPagoId()== null) {
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

    private boolean validaRegistro(String periodoId,Date fechaInicio,Date fechaFin,Double limiteInferior,Double limiteSuperior,Double cuota)
            throws ServiceException {
        try {
            return csTarifaPeriodicaIsrRepository.
                    existsByIdPeriodicidadPagoAndFechaInicioAndLimite(periodoId,fechaInicio,fechaFin,limiteInferior,limiteSuperior,cuota).isEmpty();
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaRegistro " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private boolean validaRegistro(List<CsTarifaPeriodicaIsr> valoresTablaPeriodicaISR)
            throws ServiceException {
        try {
            for (CsTarifaPeriodicaIsr isr : valoresTablaPeriodicaISR){
                List<CsTarifaPeriodicaIsr> listaMontoLimites = valoresTablaPeriodicaISR.stream().filter(csTarifaPeriodicaIsr ->
                                csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                        csTarifaPeriodicaIsr.getCuotaFija().doubleValue() == isr.getCuotaFija().doubleValue() &&
                                        csTarifaPeriodicaIsr.getLimiteInferior().doubleValue() <= isr.getLimiteInferior().doubleValue() &&
                                        csTarifaPeriodicaIsr.getLimiteSuperior().doubleValue() >= isr.getLimiteInferior().doubleValue()
                                        ||
                                        csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                                csTarifaPeriodicaIsr.getCuotaFija().doubleValue() == isr.getCuotaFija().doubleValue() &&
                                                csTarifaPeriodicaIsr.getLimiteInferior().doubleValue() <= isr.getLimiteSuperior().doubleValue() &&
                                                csTarifaPeriodicaIsr.getLimiteSuperior().doubleValue() >= isr.getLimiteSuperior().doubleValue()).
                        collect(Collectors.toList());
                List<CsTarifaPeriodicaIsr> listaFechas = valoresTablaPeriodicaISR.stream().filter(csTarifaPeriodicaIsr ->
                                (csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                        csTarifaPeriodicaIsr.getFechaInicio().getTime() <= isr.getFechaInicio().getTime() &&
                                        csTarifaPeriodicaIsr.getFechaFin().getTime() >= isr.getFechaInicio().getTime()
                                        ||
                                        csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                                csTarifaPeriodicaIsr.getFechaInicio().getTime() <= isr.getFechaFin().getTime() &&
                                                csTarifaPeriodicaIsr.getFechaFin().getTime() >= isr.getFechaFin().getTime())
                                        ||
                                        (csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                                isr.getFechaInicio().getTime() <= csTarifaPeriodicaIsr.getFechaInicio().getTime() &&
                                                isr.getFechaFin().getTime() >= csTarifaPeriodicaIsr.getFechaInicio().getTime()
                                                ||
                                                csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                                        isr.getFechaInicio().getTime() <= csTarifaPeriodicaIsr.getFechaFin().getTime() &&
                                                        isr.getFechaFin().getTime() >= csTarifaPeriodicaIsr.getFechaFin().getTime())).
                        collect(Collectors.toList());

                boolean esFechas = listaMontoLimites.stream().noneMatch(csTarifaPeriodicaIsr ->
                        (csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                csTarifaPeriodicaIsr.getFechaInicio().getTime() <= isr.getFechaInicio().getTime() &&
                                csTarifaPeriodicaIsr.getFechaFin().getTime() >= isr.getFechaInicio().getTime()
                                ||
                                csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                        csTarifaPeriodicaIsr.getFechaInicio().getTime() <= isr.getFechaFin().getTime() &&
                                        csTarifaPeriodicaIsr.getFechaFin().getTime() >= isr.getFechaFin().getTime())
                                ||
                                (csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                        isr.getFechaInicio().getTime() <= csTarifaPeriodicaIsr.getFechaInicio().getTime() &&
                                        isr.getFechaFin().getTime() >= csTarifaPeriodicaIsr.getFechaInicio().getTime()
                                        ||
                                        csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                                isr.getFechaInicio().getTime() <= csTarifaPeriodicaIsr.getFechaFin().getTime() &&
                                                isr.getFechaFin().getTime() >= csTarifaPeriodicaIsr.getFechaFin().getTime()));

                boolean esLimites = listaFechas.stream().noneMatch(csTarifaPeriodicaIsr ->
                        csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                csTarifaPeriodicaIsr.getCuotaFija().doubleValue() == isr.getCuotaFija().doubleValue() &&
                                csTarifaPeriodicaIsr.getLimiteInferior().doubleValue() <= isr.getLimiteInferior().doubleValue() &&
                                csTarifaPeriodicaIsr.getLimiteSuperior().doubleValue() >= isr.getLimiteInferior().doubleValue()
                                ||
                                csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                        csTarifaPeriodicaIsr.getCuotaFija().doubleValue() == isr.getCuotaFija().doubleValue() &&
                                        csTarifaPeriodicaIsr.getLimiteInferior().doubleValue() <= isr.getLimiteSuperior().doubleValue() &&
                                        csTarifaPeriodicaIsr.getLimiteSuperior().doubleValue() >= isr.getLimiteSuperior().doubleValue());

                List<CsTarifaPeriodicaIsr> listaFechasResultado = listaFechas.stream().filter(csTarifaPeriodicaIsr ->
                        csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                csTarifaPeriodicaIsr.getCuotaFija().doubleValue() == isr.getCuotaFija().doubleValue() &&
                                csTarifaPeriodicaIsr.getLimiteInferior().doubleValue() <= isr.getLimiteInferior().doubleValue() &&
                                csTarifaPeriodicaIsr.getLimiteSuperior().doubleValue() >= isr.getLimiteInferior().doubleValue()
                                ||
                                csTarifaPeriodicaIsr.getTarifaPeriodicaIsrId() != isr.getTarifaPeriodicaIsrId() &&
                                        csTarifaPeriodicaIsr.getCuotaFija().doubleValue() == isr.getCuotaFija().doubleValue() &&
                                        csTarifaPeriodicaIsr.getLimiteInferior().doubleValue() <= isr.getLimiteSuperior().doubleValue() &&
                                        csTarifaPeriodicaIsr.getLimiteSuperior().doubleValue() >= isr.getLimiteSuperior().doubleValue()).collect(Collectors.toList());


                List<CsTarifaPeriodicaIsr> listaMontoResultado = listaMontoLimites.stream().filter(csTarifaPeriodicaIsr ->
                        (csTarifaPeriodicaIsr.getFechaInicio().getTime() <= isr.getFechaInicio().getTime() &&
                                csTarifaPeriodicaIsr.getFechaFin().getTime() >= isr.getFechaInicio().getTime() ||
                                csTarifaPeriodicaIsr.getFechaInicio().getTime() <= isr.getFechaFin().getTime() &&
                                        csTarifaPeriodicaIsr.getFechaFin().getTime() >= isr.getFechaFin().getTime())
                                ||
                                (isr.getFechaInicio().getTime() <= csTarifaPeriodicaIsr.getFechaInicio().getTime() &&
                                        isr.getFechaFin().getTime() >= csTarifaPeriodicaIsr.getFechaInicio().getTime() ||
                                        isr.getFechaInicio().getTime() <= csTarifaPeriodicaIsr.getFechaFin().getTime() &&
                                                isr.getFechaFin().getTime() >= csTarifaPeriodicaIsr.getFechaFin().getTime())).collect(Collectors.toList());

                if(!esFechas && !esLimites){
                    return false;
                }

            }
            return true;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaRegistro " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatoriosModificar(List<CsTarifaPeriodicaIsr> lista) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (!lista.stream().noneMatch(csTarifaPeriodicaIsr -> csTarifaPeriodicaIsr.getLimiteInferior() == null
                    || csTarifaPeriodicaIsr.getLimiteSuperior() == null || csTarifaPeriodicaIsr.getCuotaFija() == null
                    || csTarifaPeriodicaIsr.getFechaInicio() == null || csTarifaPeriodicaIsr.getFechaFin() == null)) {
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

}
