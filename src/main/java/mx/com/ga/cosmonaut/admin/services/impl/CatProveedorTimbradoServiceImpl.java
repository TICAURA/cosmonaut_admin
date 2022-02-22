package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.CatProveedorTimbradoService;
import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatProveedorTimbrado;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.catalogo.negocio.CatProveedorTimbradoRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;
import mx.com.ga.cosmonaut.common.util.Utilidades;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CatProveedorTimbradoServiceImpl implements CatProveedorTimbradoService {

    @Inject
    private CatProveedorTimbradoRepository catProveedorTimbradoRepository;

    @Override
    public RespuestaGenerica listar() throws ServiceException {
        try {
            return new RespuestaGenerica(catProveedorTimbradoRepository.findAll(),
                    Constantes.RESULTADO_EXITO,
                    Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " listar " + Constantes.ERROR_EXCEPCION, e);
        }

    }

    @Override
    public RespuestaGenerica obtenerId(Integer idProveedorTimbrado) throws ServiceException {
        try {
            return new RespuestaGenerica(catProveedorTimbradoRepository.findById(idProveedorTimbrado),
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
            return new RespuestaGenerica(catProveedorTimbradoRepository.findByEsActivoOrderByDescripcion(activo),
                    Constantes.RESULTADO_EXITO,
                    Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " listarActivos " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica modificar(CatProveedorTimbrado proveedorTimbrado) throws ServiceException {
        try {
            RespuestaGenerica respuesta = validarCamposObligatorios(proveedorTimbrado);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(proveedorTimbrado);
                if (respuesta.isResultado()) {
                    respuesta.setDatos(catProveedorTimbradoRepository.update(proveedorTimbrado));
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
    public RespuestaGenerica guardar(CatProveedorTimbrado proveedorTimbrado) throws ServiceException {
        try {
            RespuestaGenerica respuesta = validarCamposObligatorios(proveedorTimbrado);
            if (respuesta.isResultado()) {
                respuesta = validarEstructura(proveedorTimbrado);
                if (respuesta.isResultado()) {
                    proveedorTimbrado.setFechaAlta(Utilidades.obtenerFechaSystema());
                    proveedorTimbrado.setEsActivo(Constantes.ESTATUS_INACTIVO);
                    respuesta.setDatos(catProveedorTimbradoRepository.save(proveedorTimbrado));
                    respuesta.setMensaje(Constantes.GUARDADO_PROVEEDOR_TIMBRADO);
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
            return new RespuestaGenerica(catProveedorTimbradoRepository.findByDescripcionIlikeOrderByDescripcion("%" + descripcion.getDescripcion() + "%"),
                    Constantes.RESULTADO_EXITO,Constantes.EXITO);
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " listarEstatusDescricpcion " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(CatProveedorTimbrado proveedorTimbrado) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica(null, Constantes.RESULTADO_EXITO, Constantes.EXITO);
            if (proveedorTimbrado.getDescripcion() == null
                    || proveedorTimbrado.getDescripcion().isEmpty()) {
                respuesta.setMensaje(Constantes.CAMPOS_REQUERIDOS);
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
            }
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " validarCamposObligatorios " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarEstructura(CatProveedorTimbrado proveedorTimbrado) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica(null, Constantes.RESULTADO_EXITO, Constantes.EXITO);
            if (catProveedorTimbradoRepository.existsByDescripcion(proveedorTimbrado.getDescripcion())) {
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
