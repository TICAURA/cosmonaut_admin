package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.CatTasaAplicableIsnService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatTasaAplicableIsn;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.catalogo.negocio.CatTasaAplicableIsnRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class CatTasaAplicableIsnServiceImpl implements CatTasaAplicableIsnService {

    @Inject
    private CatTasaAplicableIsnRepository catTasaAplicableIsnRepository;

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(catTasaAplicableIsnRepository.findByEsActivo(activo));
            respuesta.setMensaje(Constantes.EXITO);
           // respuesta.setResultado(Constantes.RESULTADmodificarMultipleO_EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    public RespuestaGenerica guardar(CatTasaAplicableIsn catTasaAplicableIsn) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(catTasaAplicableIsn);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(catTasaAplicableIsn);
                if (respuesta.isResultado()) {
                    catTasaAplicableIsn.setEsActivo(Constantes.ESTATUS_ACTIVO);
                    respuesta.setDatos(catTasaAplicableIsnRepository.save(catTasaAplicableIsn));
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
    
    public RespuestaGenerica modificar(CatTasaAplicableIsn catTasaAplicableIsn) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(catTasaAplicableIsn);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(catTasaAplicableIsn);
                if (respuesta.isResultado()) {
                    respuesta.setDatos(catTasaAplicableIsnRepository.update(catTasaAplicableIsn));
                    respuesta.setMensaje(Constantes.EXITO);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                }
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificaEstados " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica modificarMultiple(List<CatTasaAplicableIsn> valoresTasaAplicableIsn) throws ServiceException {
        try {
            List<RespuestaGenerica> lista = new ArrayList<>();
            for(int i=0;i<valoresTasaAplicableIsn.size();i++){
                CatTasaAplicableIsn valor = valoresTasaAplicableIsn.get(i);
                RespuestaGenerica respuesta;
                respuesta = validarCamposObligatorios(valor);
                if (respuesta.isResultado()) {
                    //validamos si los limites superior e inferiores son correctos repetidos dentro de la opcion multiple
                    if(valor.getLimiteInferior().compareTo(valor.getLimiteSuperior())>=0)
                        return new RespuestaGenerica(null,Constantes.RESULTADO_ERROR,Constantes.ERROR_TARIFA_PERIODICA_GENERICO);
                    //Validamos que no se repitan las fechas de inicio si se repite los limites no deben ser traslapados
                    List<CatTasaAplicableIsn> listaIsr =valoresTasaAplicableIsn;
                    for(int j=i+1;j<valoresTasaAplicableIsn.size();j++) {
                        CatTasaAplicableIsn item = listaIsr.get(j);
                        if(valor.getFechaInicio().equals(item.getFechaInicio()) && valor.getTasaAplicableIsnId()!= item.getTasaAplicableIsnId()){
                            if(item.getLimiteSuperior().compareTo(valor.getLimiteInferior())<0 || item.getLimiteInferior().compareTo(valor.getLimiteSuperior())>0);
                            else
                                return new RespuestaGenerica(null,Constantes.RESULTADO_ERROR,Constantes.ERROR_TARIFA_PERIODICA_GENERICO);
                        }
                    }
                        respuesta.setMensaje(Constantes.EXITO);
                        respuesta.setResultado(Constantes.RESULTADO_EXITO);
                        lista.add(respuesta);
                }else {
                    lista.add(respuesta);
                    break;
                }
            }

            if(lista.stream().anyMatch(respuestaGenerica -> !respuestaGenerica.isResultado())){
                return new RespuestaGenerica(null,Constantes.RESULTADO_ERROR,Constantes.ERROR_TARIFA_PERIODICA_GENERICO);
            }

            Set<CatTasaAplicableIsn> dat = new HashSet<>();
            for(CatTasaAplicableIsn item : valoresTasaAplicableIsn){
                        item.setEsActivo(Constantes.ESTATUS_ACTIVO);
                        dat.add(this.catTasaAplicableIsnRepository.update(item));
            }

            return new RespuestaGenerica(dat,Constantes.RESULTADO_EXITO,Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificarMultipleISN " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(CatTasaAplicableIsn catTasaAplicableIsn) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (catTasaAplicableIsn.getFechaInicio() == null
                    || catTasaAplicableIsn.getLimiteInferior() == null
                    || catTasaAplicableIsn.getCestado() == null
                    || catTasaAplicableIsn.getCestado().getEstadoId()== null){
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

    private RespuestaGenerica validarEstructura(CatTasaAplicableIsn catTasaAplicableIsn) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (validaDuplicado(catTasaAplicableIsn.getCestado().getEstadoId(),catTasaAplicableIsn.getCuotaFija().doubleValue(),
                    catTasaAplicableIsn.getLimiteInferior().doubleValue(),catTasaAplicableIsn.getLimiteSuperior().doubleValue(),
                    catTasaAplicableIsn.getFechaInicio()) ) {
                if(catTasaAplicableIsn.getLimiteInferior().equals(catTasaAplicableIsn.getLimiteSuperior())){
                    respuesta.setMensaje("No se puede colocar el mismo limite inferior que el superior");
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                }else {
                    respuesta.setMensaje(Constantes.EXITO);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                }
            } else {
                respuesta.setMensaje("El registro se traslapa en los límites de los catálogos existentes");
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
            }
            if (!validarEstructuraFecha(catTasaAplicableIsn))
            {
                respuesta.setMensaje(Constantes.FECHAS_TRASLAPE);
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
            }

            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validarEstructura " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private boolean validarEstructuraFecha(CatTasaAplicableIsn catTasaAplicableIsn) throws ServiceException {
        try {
            List<CatTasaAplicableIsn> lista  = catTasaAplicableIsnRepository.
                    findByIdEstadoEstadoTraslaparFechas(catTasaAplicableIsn.getCestado().getEstadoId(),catTasaAplicableIsn.getFechaInicio(),catTasaAplicableIsn.getFechaFin());
            return lista.isEmpty();
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaDuplicado " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private boolean validaDuplicado(Integer estado, Double cuotaFija,Double limiteInferior,Double limiteSuperior, Date fecha) throws ServiceException {
        try {
            List<CatTasaAplicableIsn> listaTasa = catTasaAplicableIsnRepository.
                    findByIdEstadoEstadoIdAndLimitesAndCuotaFija(estado,limiteInferior,limiteSuperior,cuotaFija,fecha);
            if (listaTasa.isEmpty()){
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaDuplicado " + Constantes.ERROR_EXCEPCION, e);
        }
    }
}
