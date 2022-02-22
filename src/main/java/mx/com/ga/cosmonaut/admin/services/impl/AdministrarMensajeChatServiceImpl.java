package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.AdministrarMensajeChatService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmMensajeChatCentrocostos;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmMensajeChatCentrocostosRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AdministrarMensajeChatServiceImpl implements AdministrarMensajeChatService {

    @Inject
    private AdmMensajeChatCentrocostosRepository mensajeChatCentrocostosRepository;

    @Override
    public RespuestaGenerica guardar(AdmMensajeChatCentrocostos admMensajeChatCentrocostos) throws ServiceException {
        try {
            RespuestaGenerica respuestaGenerica = validarCamposObligatorios(admMensajeChatCentrocostos);
            if (respuestaGenerica.isResultado()){
                respuestaGenerica.setDatos(mensajeChatCentrocostosRepository.save(admMensajeChatCentrocostos));
            }
            return respuestaGenerica;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " guardar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica modificar(AdmMensajeChatCentrocostos admMensajeChatCentrocostos) throws ServiceException {
        try{
            if (admMensajeChatCentrocostos.getMensajeChatCentrocostosId() != null){
                RespuestaGenerica respuesta = validarCamposObligatorios(admMensajeChatCentrocostos);
                if (respuesta.isResultado()){
                    respuesta.setDatos(mensajeChatCentrocostosRepository.update(admMensajeChatCentrocostos));
                }
                return respuesta;
            }else{
                return new RespuestaGenerica(null,Constantes.RESULTADO_ERROR,Constantes.ID_NULO);
            }
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" modificar " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica obtenerId(Integer mensajeChatCentrocostosId) throws ServiceException {
        try{
            return new RespuestaGenerica(mensajeChatCentrocostosRepository.findById(mensajeChatCentrocostosId).orElse(new AdmMensajeChatCentrocostos()),
                    Constantes.RESULTADO_EXITO,Constantes.EXITO);
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" obtenerId " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica listarEmpresa(Integer idEmpresa) throws ServiceException {
        try{
            return new RespuestaGenerica(mensajeChatCentrocostosRepository.findByCentrocClienteIdCentrocClienteId(idEmpresa),
                    Constantes.RESULTADO_EXITO,Constantes.EXITO);
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" listarEmpresa " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    @Override
    public RespuestaGenerica listarEmpresaUsuario(Integer idEmpresa, Integer idUsuario) throws ServiceException {
        try{
            return new RespuestaGenerica(mensajeChatCentrocostosRepository.
                    findByCentrocClienteIdCentrocClienteIdAndUsuarioIdUsuarioId(idEmpresa, idUsuario),
                    Constantes.RESULTADO_EXITO,Constantes.EXITO);
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" listarEmpresa " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private RespuestaGenerica validarCamposObligatorios(AdmMensajeChatCentrocostos admMensajeChatCentrocostos) throws ServiceException {
        try{
            RespuestaGenerica respuesta = new RespuestaGenerica();
            if(admMensajeChatCentrocostos.getCentrocClienteId() == null
                    || admMensajeChatCentrocostos.getCentrocClienteId().getCentrocClienteId() == null
                    || admMensajeChatCentrocostos.getUsuarioId() == null
                    || admMensajeChatCentrocostos.getUsuarioId().getUsuarioId() == null
                    || admMensajeChatCentrocostos.getMensajeGenerico() == null
                    || admMensajeChatCentrocostos.getMensajeGenerico().isEmpty()
                    || admMensajeChatCentrocostos.getTipoMensajeId() == null
                    || admMensajeChatCentrocostos.getTipoMensajeId().getTipoMensajeId() == null){
                respuesta.setResultado(Constantes.RESULTADO_ERROR);
                respuesta.setMensaje(Constantes.CAMPOS_REQUERIDOS);
            }else {
                respuesta.setResultado(Constantes.RESULTADO_EXITO);
                respuesta.setMensaje(Constantes.EXITO);
            }
            return respuesta;
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" validaCamposObligatorios " + Constantes.ERROR_EXCEPCION, e);
        }
    }

}
