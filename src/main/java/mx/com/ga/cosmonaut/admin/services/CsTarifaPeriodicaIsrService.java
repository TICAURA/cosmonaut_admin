package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.sat.CsTarifaPeriodicaIsr;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

import java.util.List;
import java.util.Set;

public interface CsTarifaPeriodicaIsrService {

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;
    
    RespuestaGenerica guardar(CsTarifaPeriodicaIsr csTarifaPeriodicaIsr) throws ServiceException;

    RespuestaGenerica modificar(CsTarifaPeriodicaIsr csTarifaPeriodicaIsr) throws ServiceException;

    RespuestaGenerica modificarMultiple(List<CsTarifaPeriodicaIsr> valoresTablaPeriodicaISR) throws ServiceException;
}
