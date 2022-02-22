package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.PwdService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.pwd.GenerarResponse;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.util.Constantes;
import mx.com.ga.cosmonaut.common.util.PwdUtil;

import javax.inject.Singleton;

@Singleton
public class PwdServiceImpl implements PwdService {

    @Override
    public RespuestaGenerica generar(short lenght) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();

            String pwd = PwdUtil.generatePwd(lenght);

            respuesta.setDatos(new GenerarResponse(pwd));
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            respuesta.setMensaje(Constantes.EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" generar " + Constantes.ERROR_EXCEPCION, e);
        }
    }
}
