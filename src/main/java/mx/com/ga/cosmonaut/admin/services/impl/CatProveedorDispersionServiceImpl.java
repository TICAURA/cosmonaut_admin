package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.CatProveedorDispersionService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatProveedorDispersion;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.catalogo.negocio.CatProveedorDispersionRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CatProveedorDispersionServiceImpl implements CatProveedorDispersionService {

    @Inject
    private CatProveedorDispersionRepository catProveedorDispersionRepository;

    @Override
    public RespuestaGenerica listar() throws ServiceException {
        try {
            return new RespuestaGenerica(catProveedorDispersionRepository.findAll(),
                    Constantes.RESULTADO_EXITO,
                    Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " listar " + Constantes.ERROR_EXCEPCION, e);
        }

    }

    @Override
    public RespuestaGenerica obtenerId(Integer idProveedorDispersion) throws ServiceException {
        try {
            return new RespuestaGenerica(catProveedorDispersionRepository.findById(idProveedorDispersion),
                    Constantes.RESULTADO_EXITO,
                    Constantes.EXITO);
        } catch (Exception e) {

            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " obtenerId " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica listarActivos(Boolean activo) throws ServiceException {
        try {
            return new RespuestaGenerica(catProveedorDispersionRepository.findByEsActivoOrderByDescripcion(activo),
                    Constantes.RESULTADO_EXITO,
                    Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " listarActivos " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica modificar(CatProveedorDispersion proveedorDispersion) throws ServiceException {
        try {
            RespuestaGenerica respuesta = validarCamposObligatorios(proveedorDispersion);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(proveedorDispersion);
                if (respuesta.isResultado()) {
                    respuesta.setDatos(catProveedorDispersionRepository.update(proveedorDispersion));
                    respuesta.setMensaje(Constantes.EXITO);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                }
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " modificar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica guardar(CatProveedorDispersion proveedorDispersion) throws ServiceException {
        try {
            RespuestaGenerica respuesta = validarCamposObligatorios(proveedorDispersion);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(proveedorDispersion);
                if (respuesta.isResultado()) {
                    proveedorDispersion.setFechaAlta(Utilidades.obtenerFechaSystema());
                    proveedorDispersion.setEsActivo(Constantes.ESTATUS_INACTIVO);
                    respuesta.setDatos(catProveedorDispersionRepository.save(proveedorDispersion));
                    respuesta.setMensaje(Constantes.GUARDADO_PROVEEDOR_DISPERSION);
                    respuesta.setResultado(Constantes.RESULTADO_EXITO);
                }
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " guardar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException {
        try {
            return new RespuestaGenerica(catProveedorDispersionRepository.findByDescripcionIlikeOrderByDescripcion("%" + descripcion.getDescripcion() + "%"),
                    Constantes.RESULTADO_EXITO,Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " listarEstatusDescricpcion " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(CatProveedorDispersion proveedorDispersion) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica(null, Constantes.RESULTADO_EXITO, Constantes.EXITO);
            if (proveedorDispersion.getDescripcion() == null
                    || proveedorDispersion.getDescripcion().isEmpty()) {
                respuesta.setMensaje(Constantes.CAMPOS_REQUERIDOS);
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validarCamposObligatorios " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarEstructura(CatProveedorDispersion proveedorDispersion) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica(null, Constantes.RESULTADO_EXITO, Constantes.EXITO);
            if (catProveedorDispersionRepository.existsByDescripcion(proveedorDispersion.getDescripcion())) {
                respuesta.setMensaje("Ya existe un registro con el mismo nombre en el catálogo");
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validarEstructura " + Constantes.ERROR_EXCEPCION, e);
        }
    }


}
