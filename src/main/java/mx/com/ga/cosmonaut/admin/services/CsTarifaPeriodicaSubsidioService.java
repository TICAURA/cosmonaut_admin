package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTarifaPeriodicaSubsidio;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

import java.util.Set;

public interface CsTarifaPeriodicaSubsidioService {
    
    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;

    RespuestaGenerica guardar(CsTarifaPeriodicaSubsidio csTarifaPeriodicaSubsidio) throws ServiceException;
    
    RespuestaGenerica modificar(CsTarifaPeriodicaSubsidio csTarifaPeriodicaSubsidio) throws ServiceException;

    RespuestaGenerica modificarMultiple(Set<CsTarifaPeriodicaSubsidio> valoresTablaPeriodicaSubsidio) throws ServiceException;

}
