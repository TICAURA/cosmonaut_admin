package mx.com.ga.cosmonaut.admin.services.impl;

import javax.inject.Inject;
import javax.inject.Singleton;
import mx.com.ga.cosmonaut.admin.services.CsRegimenFiscalService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsRegimenFiscal;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsRegimenFiscalRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;

@Singleton
public class CsRegimenFiscalServiceImpl implements CsRegimenFiscalService{

    @Inject
    private CsRegimenFiscalRepository csRegimenFiscalRepository;

    public RespuestaGenerica findAll() throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(csRegimenFiscalRepository.findAll());
            respuesta.setMensaje(Constantes.EXITO);
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            return respuesta;
        } catch (Exception e) {

            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findAll " + Constantes.ERROR_EXCEPCION, e);
        }

    }

    public RespuestaGenerica findById(String id) throws ServiceException {
        try {
            RespuestaGenerica response = new RespuestaGenerica();
            response.setDatos(csRegimenFiscalRepository.findById(id).orElse(new CsRegimenFiscal()));
            response.setResultado(true);
            response.setMensaje(Constantes.EXITO);
            return response;
        } catch (Exception e) {

            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findById " + Constantes.ERROR_EXCEPCION, e);
        }
    }
    
    @Override
    public RespuestaGenerica findEsActivo(Boolean activo) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(csRegimenFiscalRepository.findByActivoNoConcat(activo));
            respuesta.setResultado(true);
            respuesta.setMensaje(Constantes.EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }
    
    public RespuestaGenerica modificar(CsRegimenFiscal csRegimenFiscal)throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csRegimenFiscal);
            if (respuesta.isResultado()) {
                respuesta.setDatos(csRegimenFiscalRepository.update(csRegimenFiscal));
                respuesta.setMensaje(Constantes.EXITO);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificaEstados " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    
    public RespuestaGenerica guardar(CsRegimenFiscal csRegimenFiscal) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csRegimenFiscal);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(csRegimenFiscal);
                if (respuesta.isResultado()) {
                    csRegimenFiscal.setActivo(Constantes.ESTATUS_ACTIVO);
                    respuesta.setDatos(csRegimenFiscalRepository.save(csRegimenFiscal));
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
            return new RespuestaGenerica(csRegimenFiscalRepository.findByDescripcionIlike("%" + descripcion.getDescripcion() + "%"),
                    Constantes.RESULTADO_EXITO,Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " listarEstatusDescricpcion " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(CsRegimenFiscal csRegimenFiscal) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (csRegimenFiscal.getDescripcion()== null
                    || csRegimenFiscal.getDescripcion().isEmpty()
                    || csRegimenFiscal.getRegimenfiscalId() == null
                    || csRegimenFiscal.getRegimenfiscalId().isEmpty()
                    || csRegimenFiscal.getFecInicio()== null
                    || csRegimenFiscal.getIndPersonaFisica() == null
                    || csRegimenFiscal.getIndPersonaMoral() == null){
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

    private RespuestaGenerica validarEstructura(CsRegimenFiscal csRegimenFiscal) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (validaDuplicado(csRegimenFiscal.getDescripcion(), csRegimenFiscal.getRegimenfiscalId())) {
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
            return csRegimenFiscalRepository.findByDescripcion(desc).isEmpty()
                    && csRegimenFiscalRepository.findByRegimenfiscalId(id).isEmpty();
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaDuplicado " + Constantes.ERROR_EXCEPCION, e);
        }
    }


}
