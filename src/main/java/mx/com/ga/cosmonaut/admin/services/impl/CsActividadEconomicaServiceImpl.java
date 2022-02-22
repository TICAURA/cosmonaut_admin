/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ga.cosmonaut.admin.services.impl;

import javax.inject.Inject;
import javax.inject.Singleton;
import mx.com.ga.cosmonaut.admin.services.CsActividadEconomicaService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsActividadEconomica;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.catalogo.sat.CsActividadEconomicaRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;

/**
 *
 * @author Usuario
 */
@Singleton
public class CsActividadEconomicaServiceImpl implements CsActividadEconomicaService {

    @Inject
    private CsActividadEconomicaRepository csActividadEconomicaRepository;

    @Override
    public RespuestaGenerica findAll() throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(csActividadEconomicaRepository.findAll());
            respuesta.setMensaje(Constantes.EXITO);
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            return respuesta;
        } catch (Exception e) {

            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findAll " + Constantes.ERROR_EXCEPCION, e);
        }

    }

    public RespuestaGenerica findById(Integer id) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            respuesta.setDatos(csActividadEconomicaRepository.findById(id).orElse(new CsActividadEconomica() ));
            respuesta.setResultado(true);
            respuesta.setMensaje(Constantes.EXITO);
            return respuesta;
        } catch (Exception e) {

            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findById " + Constantes.ERROR_EXCEPCION, e);
        }
    }

     public RespuestaGenerica findByEsActivo(Boolean estatus, Integer nivel) throws ServiceException {
        try {
            RespuestaGenerica response = new RespuestaGenerica();
            response.setDatos(csActividadEconomicaRepository.findByEsActivoAndNivelNoConcat(estatus, nivel));
            response.setResultado(true);
            response.setMensaje(Constantes.EXITO);
            return response;
        } catch (Exception e) {

            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivoAndNivel " + Constantes.ERROR_EXCEPCION, e);
        }
    }
     
     public RespuestaGenerica findByEsActivo(Boolean estatus, Integer nivel, Integer sector) throws ServiceException {
        try {
            RespuestaGenerica response = new RespuestaGenerica();
            response.setDatos(csActividadEconomicaRepository.findByEsActivoAndNivelNoConcat(estatus, nivel, sector));
            response.setResultado(true);
            response.setMensaje(Constantes.EXITO);
            return response;
        } catch (Exception e) {

            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivoAndNivel " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        try {
            RespuestaGenerica response = new RespuestaGenerica();
            response.setDatos(csActividadEconomicaRepository.findByEsActivoNoConcat(activo));
            response.setResultado(true);
            response.setMensaje(Constantes.EXITO);
            return response;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " findByEsActivo " + Constantes.ERROR_EXCEPCION, e);
        }
    }
    
    public RespuestaGenerica modificar(CsActividadEconomica csActividadEconomica )throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csActividadEconomica);
            if (respuesta.isResultado()) {
                respuesta.setDatos(csActividadEconomicaRepository.update(csActividadEconomica));
                respuesta.setMensaje(Constantes.EXITO);
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificaEstados " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    
    public RespuestaGenerica guardar(CsActividadEconomica csActividadEconomica ) throws ServiceException {
        RespuestaGenerica respuesta;
        try {
            respuesta = validarCamposObligatorios(csActividadEconomica);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(csActividadEconomica);
                if (respuesta.isResultado()) {
                    csActividadEconomica.setEsActivo(Constantes.ESTATUS_ACTIVO);
                    respuesta.setDatos(csActividadEconomicaRepository.save(csActividadEconomica));
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

    private RespuestaGenerica validarCamposObligatorios(CsActividadEconomica csActividadEconomica ) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (csActividadEconomica.getDescripcion()== null
                    || csActividadEconomica.getDescripcion().isEmpty()){
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

    private RespuestaGenerica validarEstructura(CsActividadEconomica csActividadEconomica ) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if (validaDuplicado(csActividadEconomica.getDescripcion())) {
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
            return csActividadEconomicaRepository.findByDescripcion(desc).isEmpty();
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validaDuplicado " + Constantes.ERROR_EXCEPCION, e);
        }
    }


}
