package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface AuthService {

    RespuestaGenerica logout(String username) throws ServiceException;

}
