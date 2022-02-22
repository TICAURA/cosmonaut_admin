package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.CsTarifaPeriodicaSubsidioService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTarifaPeriodicaIsr;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTarifaPeriodicaSubsidio;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsTarifaPeriodicaSubsidioRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class CsTarifaPeriodicaSubsidioServiceImpl implements CsTarifaPeriodicaSubsidioService {

    @Inject
    private CsTarifaPeriodicaSubsidioRepository csTarifaPeriodicaSubsidioRepository;

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        try {
            RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
            respuestaGenerica.setDatos(csTarifaPeriodicaSubsidioRepository.findByEsActivo(activo));
            respuestaGenerica.setMensaje(Constantes.EXITO);
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            return respuestaGenerica;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica guardar(CsTarifaPeriodicaSubsidio csTarifaPeriodicaSubsidio) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csTarifaPeriodicaSubsidio);
            if (respuesta.isResultado()) {
                if (this.validaRegistro(csTarifaPeriodicaSubsidio.getPeriodicidadPagoId().getPeriodicidadPagoId(),
                        csTarifaPeriodicaSubsidio.getFechaInicio(),csTarifaPeriodicaSubsidio.getFechaFin(),
                        csTarifaPeriodicaSubsidio.getLimiteInferior().doubleValue(),csTarifaPeriodicaSubsidio.getLimiteSuperior().doubleValue(),
                        csTarifaPeriodicaSubsidio.getMontoSubsidio().doubleValue())){
                    csTarifaPeriodicaSubsidio.setEsActivo(Constantes.ESTATUS_ACTIVO);
                    respuesta.setDatos(csTarifaPeriodicaSubsidioRepository.save(csTarifaPeriodicaSubsidio));
                    respuesta.setMensaje(Constantes.EXITO);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                }else {
                    respuesta = new RespuestaGenerica(null,Constantes.RESULTADO_ERROR,Constantes.ERROR_TARIFA_PERIODICA_ISR_PERIDO_EXISTENTE);
                }

            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " guardaEstados " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
     public RespuestaGenerica modificar(CsTarifaPeriodicaSubsidio csTarifaPeriodicaSubsidio) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csTarifaPeriodicaSubsidio);
            if (respuesta.isResultado()) {
                if (this.validaRegistro(csTarifaPeriodicaSubsidio.getPeriodicidadPagoId().getPeriodicidadPagoId(),
                        csTarifaPeriodicaSubsidio.getFechaInicio(),csTarifaPeriodicaSubsidio.getFechaFin(),
                        csTarifaPeriodicaSubsidio.getLimiteInferior().doubleValue(),csTarifaPeriodicaSubsidio.getLimiteSuperior().doubleValue(),
                        csTarifaPeriodicaSubsidio.getMontoSubsidio().doubleValue())){
                    respuesta.setDatos(csTarifaPeriodicaSubsidioRepository.update(csTarifaPeriodicaSubsidio));
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
    public RespuestaGenerica modificarMultiple(Set<CsTarifaPeriodicaSubsidio> valoresTablaPeriodicaSubsidio) throws ServiceException {
        try {

            RespuestaGenerica respuesta = validarCamposObligatoriosModificar(valoresTablaPeriodicaSubsidio);
            if (!respuesta.isResultado()) {
                return respuesta;
            }

            List<RespuestaGenerica> lista = new ArrayList<>();

            for (CsTarifaPeriodicaSubsidio subsidio : valoresTablaPeriodicaSubsidio){
                List<CsTarifaPeriodicaSubsidio> listaMontoLimites = valoresTablaPeriodicaSubsidio.stream().filter(csTarifaPeriodicaSubsidio ->
                                csTarifaPeriodicaSubsidio.getTarifaPeriodicaSubsidioId() != subsidio.getTarifaPeriodicaSubsidioId() &&
                                        csTarifaPeriodicaSubsidio.getMontoSubsidio().doubleValue() == subsidio.getMontoSubsidio().doubleValue() &&
                                        csTarifaPeriodicaSubsidio.getLimiteInferior().doubleValue() <= subsidio.getLimiteInferior().doubleValue() &&
                                        csTarifaPeriodicaSubsidio.getLimiteSuperior().doubleValue() >= subsidio.getLimiteInferior().doubleValue()
                                        ||
                                        csTarifaPeriodicaSubsidio.getTarifaPeriodicaSubsidioId() != subsidio.getTarifaPeriodicaSubsidioId() &&
                                                csTarifaPeriodicaSubsidio.getMontoSubsidio().doubleValue() == subsidio.getMontoSubsidio().doubleValue() &&
                                                csTarifaPeriodicaSubsidio.getLimiteInferior().doubleValue() <= subsidio.getLimiteSuperior().doubleValue() &&
                                                csTarifaPeriodicaSubsidio.getLimiteSuperior().doubleValue() >= subsidio.getLimiteSuperior().doubleValue()).
                        collect(Collectors.toList());

                List<CsTarifaPeriodicaSubsidio> listaFechas = valoresTablaPeriodicaSubsidio.stream().filter(csTarifaPeriodicaSubsidio ->
                        (csTarifaPeriodicaSubsidio.getFechaInicio().getTime() <= subsidio.getFechaInicio().getTime() &&
                                csTarifaPeriodicaSubsidio.getFechaFin().getTime() >= subsidio.getFechaInicio().getTime() ||
                                csTarifaPeriodicaSubsidio.getFechaInicio().getTime() <= subsidio.getFechaFin().getTime() &&
                                        csTarifaPeriodicaSubsidio.getFechaFin().getTime() >= subsidio.getFechaFin().getTime())
                                ||
                                (subsidio.getFechaInicio().getTime() <= csTarifaPeriodicaSubsidio.getFechaInicio().getTime() &&
                                        subsidio.getFechaFin().getTime() >= csTarifaPeriodicaSubsidio.getFechaInicio().getTime() ||
                                        subsidio.getFechaInicio().getTime() <= csTarifaPeriodicaSubsidio.getFechaFin().getTime() &&
                                                subsidio.getFechaFin().getTime() >= csTarifaPeriodicaSubsidio.getFechaFin().getTime())).
                        collect(Collectors.toList());


                boolean esFechas = listaMontoLimites.stream().noneMatch(csTarifaPeriodicaSubsidio ->
                        (csTarifaPeriodicaSubsidio.getFechaInicio().getTime() <= subsidio.getFechaInicio().getTime() &&
                                csTarifaPeriodicaSubsidio.getFechaFin().getTime() >= subsidio.getFechaInicio().getTime() ||
                                csTarifaPeriodicaSubsidio.getFechaInicio().getTime() <= subsidio.getFechaFin().getTime() &&
                                        csTarifaPeriodicaSubsidio.getFechaFin().getTime() >= subsidio.getFechaFin().getTime())
                                ||
                                (subsidio.getFechaInicio().getTime() <= csTarifaPeriodicaSubsidio.getFechaInicio().getTime() &&
                                        subsidio.getFechaFin().getTime() >= csTarifaPeriodicaSubsidio.getFechaInicio().getTime() ||
                                        subsidio.getFechaInicio().getTime() <= csTarifaPeriodicaSubsidio.getFechaFin().getTime() &&
                                                subsidio.getFechaFin().getTime() >= csTarifaPeriodicaSubsidio.getFechaFin().getTime()));

                boolean esLimites = listaFechas.stream().noneMatch(csTarifaPeriodicaSubsidio ->
                        csTarifaPeriodicaSubsidio.getTarifaPeriodicaSubsidioId() != subsidio.getTarifaPeriodicaSubsidioId() &&
                                csTarifaPeriodicaSubsidio.getMontoSubsidio().doubleValue() == subsidio.getMontoSubsidio().doubleValue() &&
                                csTarifaPeriodicaSubsidio.getLimiteInferior().doubleValue() <= subsidio.getLimiteInferior().doubleValue() &&
                                csTarifaPeriodicaSubsidio.getLimiteSuperior().doubleValue() >= subsidio.getLimiteInferior().doubleValue()
                                ||
                                csTarifaPeriodicaSubsidio.getTarifaPeriodicaSubsidioId() != subsidio.getTarifaPeriodicaSubsidioId() &&
                                        csTarifaPeriodicaSubsidio.getMontoSubsidio().doubleValue() == subsidio.getMontoSubsidio().doubleValue() &&
                                        csTarifaPeriodicaSubsidio.getLimiteInferior().doubleValue() <= subsidio.getLimiteSuperior().doubleValue() &&
                                        csTarifaPeriodicaSubsidio.getLimiteSuperior().doubleValue() >= subsidio.getLimiteSuperior().doubleValue());

                if(esFechas && esLimites){
                    respuesta.setMensaje(Constantes.EXITO);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                    lista.add(respuesta);
                }else {
                    respuesta.setMensaje(Constantes.ERROR);
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                    lista.add(respuesta);
                    break;
                }

            }

            if(lista.stream().anyMatch(respuestaGenerica -> !respuestaGenerica.isResultado())){
                return new RespuestaGenerica(null,Constantes.RESULTADO_ERROR,Constantes.ERROR_TARIFA_PERIODICA_GENERICO);
            }

            Set<CsTarifaPeriodicaSubsidio> data = new HashSet<>();
            valoresTablaPeriodicaSubsidio.forEach(subsidio -> {
                subsidio.setEsActivo(Constantes.ESTATUS_ACTIVO);
                data.add(csTarifaPeriodicaSubsidioRepository.update(subsidio));
            });

            return new RespuestaGenerica(data,Constantes.RESULTADO_EXITO,Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificarMultipleSub " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(CsTarifaPeriodicaSubsidio csTarifaPeriodicaSubsidio) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (csTarifaPeriodicaSubsidio.getMontoSubsidio()== null
                    || csTarifaPeriodicaSubsidio.getPeriodicidadPagoId()== null
                    || csTarifaPeriodicaSubsidio.getPeriodicidadPagoId().getPeriodicidadPagoId()== null) {
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
            return csTarifaPeriodicaSubsidioRepository.
                    existsByIdPeriodicidadPagoIdAndFechaInicioAndLimites(periodoId,fechaInicio,fechaFin,limiteInferior,limiteSuperior,cuota).isEmpty();
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaRegistro " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatoriosModificar(Set<CsTarifaPeriodicaSubsidio> lista) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (!lista.stream().noneMatch(csTarifaPeriodicaIsr -> csTarifaPeriodicaIsr.getLimiteInferior() == null
                    || csTarifaPeriodicaIsr.getLimiteSuperior() == null || csTarifaPeriodicaIsr.getMontoSubsidio() == null
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
