package mx.com.ga.cosmonaut.admin.services.impl;

import javax.inject.Inject;
import javax.inject.Singleton;
import mx.com.ga.cosmonaut.admin.services.CsTipoContratoService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTipoContrato;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsTipoContratoRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;

@Singleton
public class CsTipoContratoServiceImpl implements CsTipoContratoService {

    @Inject
    private CsTipoContratoRepository csTipoContratoRepository;

    public RespuestaGenerica findAll() throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(csTipoContratoRepository.findAll());
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
            response.setDatos(csTipoContratoRepository.findById(id).orElse(new CsTipoContrato()));
            response.setResultado(true);
            response.setMensaje(Constantes.EXITO);
            return response;
        } catch (Exception e) {

            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findById " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        try {
            RespuestaGenerica response = new RespuestaGenerica();
            response.setDatos(csTipoContratoRepository.findByEsActivoNoConcat(activo));
            response.setResultado(true);
            response.setMensaje(Constantes.EXITO);
            return response;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }

     public RespuestaGenerica modificar(CsTipoContrato csTipoContrato) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csTipoContrato);
            if (respuesta.isResultado()) {
                csTipoContrato.setDescripcion(csTipoContrato.getDescripcion().trim());
                respuesta.setDatos(csTipoContratoRepository.update(csTipoContrato));
                respuesta.setMensaje(Constantes.EXITO);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificaEstados " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    public RespuestaGenerica guardar(CsTipoContrato csTipoContrato) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csTipoContrato);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(csTipoContrato);
                if (respuesta.isResultado()) {
                    csTipoContrato.setEsActivo(Constantes.ESTATUS_ACTIVO);
                    respuesta.setDatos(csTipoContratoRepository.save(csTipoContrato));
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
            return new RespuestaGenerica(csTipoContratoRepository.findByDescripcionIlike("%" + descripcion.getDescripcion() + "%"),
                    Constantes.RESULTADO_EXITO,Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " listarEstatusDescricpcion " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(CsTipoContrato csTipoContrato) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (csTipoContrato.getDescripcion()== null
                    || csTipoContrato.getDescripcion().isEmpty()) {
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

    private RespuestaGenerica validarEstructura(CsTipoContrato csTipoContrato) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (validaDuplicado(csTipoContrato.getTipoContratoId(), csTipoContrato.getDescripcion())) {
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

    private boolean validaDuplicado(String id, String desc) throws ServiceException {
        try {
            return csTipoContratoRepository.findByIdDescripcionRepetido(id, desc.toUpperCase().trim()).isEmpty();
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaDuplicado " + Constantes.ERROR_EXCEPCION, e);
        }
    }
}
