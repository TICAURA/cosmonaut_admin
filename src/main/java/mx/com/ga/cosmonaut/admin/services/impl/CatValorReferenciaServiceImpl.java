package mx.com.ga.cosmonaut.admin.services.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import mx.com.ga.cosmonaut.common.repository.catalogo.negocio.CatValorReferenciaRepository;
import mx.com.ga.cosmonaut.admin.services.CatValorReferenciaService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatValorReferencia;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.util.Constantes;

@Singleton
public class CatValorReferenciaServiceImpl implements CatValorReferenciaService {

    @Inject
    private CatValorReferenciaRepository catValorReferenciaRepository;

    @Override
    public RespuestaGenerica findAll() throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(catValorReferenciaRepository.findAll());
            respuesta.setMensaje(Constantes.EXITO);
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            return respuesta;
        } catch (Exception e) {

            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findAll " + Constantes.ERROR_EXCEPCION, e);
        }

    }

    public RespuestaGenerica findById(Long valorReferenciaId) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(catValorReferenciaRepository.findById(valorReferenciaId).orElse(new CatValorReferencia()));
            respuesta.setResultado(true);
            respuesta.setMensaje(Constantes.EXITO);
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
            respuesta.setDatos(catValorReferenciaRepository.findByEsActivoOrderByValorReferenciaId(activo));
            respuesta.setResultado(true);
            respuesta.setMensaje(Constantes.EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }
    
    public RespuestaGenerica modificar(CatValorReferencia catValorReferencia )throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(catValorReferencia);
            if (respuesta.isResultado()) {
                //respuesta = validarEstructuraModificar(catValorReferencia);
                if (respuesta.isResultado()) {
                    Optional<CatValorReferencia> referenciaOpt = catValorReferenciaRepository.findById(catValorReferencia.getValorReferenciaId());
                    if(referenciaOpt.isPresent()){
                        CatValorReferencia referencia = referenciaOpt.get();
                        RespuestaGenerica validador = verificarValidacionesModificar(referencia,catValorReferencia);
                        if(!validador.isResultado())
                            return validador;
                        else{
                            validador = verificaValidacionInactivoA_activo(referencia,catValorReferencia);
                            if(!validador.isResultado())
                                return validador;
                            respuesta.setDatos(catValorReferenciaRepository.update(catValorReferencia));
                        }
                    }else{
                        respuesta.setResultado(Constantes.RESULTADO_ERROR);
                        respuesta.setMensaje(Constantes.MENSAJE_SIN_RESULTADOS);
                    }

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

    
    public RespuestaGenerica guardar(CatValorReferencia catValorReferencia ) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(catValorReferencia);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(catValorReferencia);
                if (respuesta.isResultado()) {
                    catValorReferencia.setEsActivo(Constantes.ESTATUS_ACTIVO);
                    respuesta.setDatos(catValorReferenciaRepository.save(catValorReferencia));
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
    public RespuestaGenerica eliminar(CatValorReferencia catValorReferencia )throws ServiceException {
        RespuestaGenerica respuesta = new RespuestaGenerica(null,Constantes.RESULTADO_EXITO,Constantes.EXITO);
        try {
            Optional<CatValorReferencia> referenciaOpt = catValorReferenciaRepository.findById(catValorReferencia.getValorReferenciaId());
            if(referenciaOpt.isPresent()){
                CatValorReferencia referencia = referenciaOpt.get();
                RespuestaGenerica validador = verificarValidacionesModificar(referencia,catValorReferencia);
                if(!validador.isResultado()){
                    return validador;
                }else{
                    validador = verificaValidacionInactivoA_activo(referencia,catValorReferencia);
                    if(!validador.isResultado()){
                        return validador;
                    }
                    respuesta.setDatos(catValorReferenciaRepository.update(catValorReferencia));
                }
            }else{
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.MENSAJE_SIN_RESULTADOS);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " eliminar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(CatValorReferencia catValorReferencia ) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (catValorReferencia.getAnioLey() == null
                    || catValorReferencia.getValor() == null){
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

    private RespuestaGenerica validarEstructura(CatValorReferencia catValorReferencia) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (validaDuplicado(catValorReferencia)) {
                if (validaPeriodo(catValorReferencia.getFechaInicio(),catValorReferencia.getFechaFin(),catValorReferencia.getTipoValorReferenciaId().getTipoValorReferenciaId())){
                    respuesta.setMensaje(Constantes.EXITO);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                }else {
                    respuesta.setMensaje("El valor de referencia ya existe en el periodo.");
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                }
            } else {
                respuesta.setMensaje(Constantes.VALOR_REFERENCIA_EXISTE);
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validarEstructura " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarEstructuraModificar(CatValorReferencia catValorReferencia) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (validaDuplicado(catValorReferencia)) {
                if (validaPeriodoModificar(catValorReferencia.getFechaInicio(),catValorReferencia.getFechaFin(),catValorReferencia.getTipoValorReferenciaId().getTipoValorReferenciaId(),catValorReferencia.getValor())){
                    respuesta.setMensaje(Constantes.EXITO);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                }else {
                    respuesta.setMensaje("El valor de referencia ya existe en el periodo.");
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                }
            } else {
                respuesta.setMensaje(Constantes.VALOR_REFERENCIA_EXISTE);
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validarEstructura " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private boolean validaDuplicado(CatValorReferencia catalogo) throws ServiceException {
        try {
            return catValorReferenciaRepository.valorRepetidoPorFechasTipoValorReferenciaId(catalogo.getFechaInicio(),catalogo.getFechaFin(),catalogo.getTipoValorReferenciaId().getTipoValorReferenciaId(),catalogo.getValor()) == 0;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaDuplicado " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private boolean validaPeriodo(Date fechaInicio,Date fechaFin,Long valorReferenciaId) throws ServiceException {
        try {
            return catValorReferenciaRepository.findByFechaBetween(fechaInicio,fechaFin,valorReferenciaId) == 0;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaPeriodo " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private boolean validaPeriodoModificar(Date fechaInicio,Date fechaFin,Long valorReferenciaId,BigDecimal valor) throws ServiceException {
        try {
            return catValorReferenciaRepository.findByFechaBetweenAndValor(fechaInicio,fechaFin,valorReferenciaId, valor.doubleValue()) == 0;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaPeriodo " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private boolean validaPeriodoExistenteValorReferencia(Date fechaInicio,Date fechaFin,Long valorReferenciaId,CatValorReferencia obj) throws ServiceException {
        try {
            boolean respuesta = true;
            if(!validaPeriodo(fechaInicio,fechaFin,valorReferenciaId)){
                List<CatValorReferencia> lista = catValorReferenciaRepository.findByFechaBetweenObj(fechaInicio,fechaFin,valorReferenciaId);
                if(lista.size() > 1){
                    respuesta = false;
                }else{
                    CatValorReferencia aux = lista.get(0);
                    respuesta = aux.getValorReferenciaId() == obj.getValorReferenciaId();
                }
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaPeriodo " + Constantes.ERROR_EXCEPCION, e);
        }
    }


    public RespuestaGenerica verificarValidacionesModificar(CatValorReferencia referencia,CatValorReferencia actual) throws ServiceException {
        RespuestaGenerica respuesta = new RespuestaGenerica();


        if(!esIgualValorReferenciaId(referencia,actual)){
            if(!validaPeriodoExistenteValorReferencia(actual.getFechaInicio(),actual.getFechaFin(),actual.getTipoValorReferenciaId().getTipoValorReferenciaId(),actual)){
                respuesta.setMensaje("El valor de referencia ya existe en el periodo.");
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                return respuesta;
            }
        }else{
            if(!esIgualFechaInicioFechaFin(referencia,actual)){
                if(!validaPeriodoExistenteValorReferencia(actual.getFechaInicio(),actual.getFechaFin(),actual.getTipoValorReferenciaId().getTipoValorReferenciaId(),actual)){
                    respuesta.setMensaje("El valor de referencia ya existe en el periodo.");
                    respuesta.setResultado(Constantes.RESULTADO_ERROR);
                    return respuesta;
                }
            }
        }

        if(!esIgualValorReferenciaId(referencia,actual)){
            if(!validaDuplicado(actual)){
                respuesta.setMensaje(Constantes.VALOR_REFERENCIA_EXISTE);
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                return respuesta;
            }
        }

        respuesta.setResultado(Constantes.RESULTADO_EXITO);
        respuesta.setMensaje(Constantes.EXITO);

        return respuesta;
    }


    public RespuestaGenerica verificaValidacionInactivoA_activo(CatValorReferencia referencia,CatValorReferencia actual) throws ServiceException {
        RespuestaGenerica respuesta = new RespuestaGenerica();

        if(!referencia.isEsActivo() && actual.isEsActivo()){
            if(!validaPeriodoExistenteValorReferencia(actual.getFechaInicio(),actual.getFechaFin(),actual.getTipoValorReferenciaId().getTipoValorReferenciaId(),actual)){
                respuesta.setMensaje("No se puede dar de alta, un valor de referencia ya existe en el periodo.");
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                return respuesta;
            }


            if(!validaDuplicado(actual)){
                respuesta.setMensaje("No se puede dar de alta un valor de referencia esta dada de alta con el mismo valor.");
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                return respuesta;
            }
        }


        respuesta.setResultado(Constantes.RESULTADO_EXITO);
        respuesta.setMensaje(Constantes.EXITO);
        return respuesta;
    }


    public boolean esIgualValorReferenciaId(CatValorReferencia referencia,CatValorReferencia actual){
        return referencia.getTipoValorReferenciaId().getTipoValorReferenciaId() ==  actual.getTipoValorReferenciaId().getTipoValorReferenciaId();
    }
    public boolean esIgualFechaInicioFechaFin(CatValorReferencia referencia,CatValorReferencia actual){
        return (referencia.getFechaInicio().compareTo(actual.getFechaInicio()) == 0 && referencia.getFechaFin().compareTo(actual.getFechaFin()) == 0) || (actual.getFechaInicio().after(referencia.getFechaInicio()) && actual.getFechaInicio().before(referencia.getFechaFin()) && actual.getFechaFin().before(referencia.getFechaFin()) && actual.getFechaFin().after(referencia.getFechaInicio()));
    }
}
