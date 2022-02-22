package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.exception.ServiceException;


public interface TablasValoresService {

    RespuestaGenerica findValorReferenciaByEsActivo(Boolean activo, Long anio) throws ServiceException;
    
    RespuestaGenerica findTarifaIsrByEsActivo(Boolean activo, String periodicidad) throws ServiceException;
    
    RespuestaGenerica findTarifaSubsidioIsrByEsActivo(Boolean activo, String periodicidad) throws ServiceException;
    
    RespuestaGenerica findTablasISRPeriodicas() throws ServiceException;
    
    RespuestaGenerica findTablasISRSubsidio() throws ServiceException;

    RespuestaGenerica findTablasISN() throws ServiceException;

    RespuestaGenerica findEstadosISN() throws ServiceException;

    RespuestaGenerica findTasaAplicableIsnByEsActivo(Boolean activo) throws ServiceException;

    RespuestaGenerica findByEsActivoAndCestadoEstadoId(Boolean activo, Integer estadoId) throws ServiceException;

    RespuestaGenerica listarValorReferenciaTodos() throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;

}
