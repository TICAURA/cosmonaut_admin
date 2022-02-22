/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.DescripcionRequest;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.catalogo.negocio.CatParentesco;
import mx.com.ga.cosmonaut.common.exception.ServiceException;


public interface CatParentescoService {

    RespuestaGenerica findAll() throws ServiceException;

    RespuestaGenerica findById(Integer id) throws ServiceException;

    RespuestaGenerica findEsActivo(Boolean activo) throws ServiceException;
    
    RespuestaGenerica modificar(CatParentesco catParentesco) throws ServiceException;
    
    RespuestaGenerica guardar(CatParentesco catParentesco) throws ServiceException;

    RespuestaGenerica listarEstatusDescripcion(DescripcionRequest descripcion) throws ServiceException;
}
