/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.ubicacion.CatEstadoAdmin;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

/**
 *
 * @author Usuario
 */
public interface CatEstadoService {

    RespuestaGenerica findAll() throws ServiceException;

    RespuestaGenerica findById(Integer idEstado) throws ServiceException;

    RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException;
    
    RespuestaGenerica guardar(CatEstadoAdmin estado) throws ServiceException;
    
    RespuestaGenerica modificar(CatEstadoAdmin estado) throws ServiceException;
    
}
