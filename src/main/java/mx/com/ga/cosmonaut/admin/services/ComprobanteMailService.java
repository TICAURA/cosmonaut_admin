package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

public interface ComprobanteMailService {

    RespuestaGenerica sendMailComprobantes(Integer nominaXperiodoId) throws ServiceException;

}
