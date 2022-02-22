package mx.com.ga.cosmonaut.admin.services;

import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.administracion.noticias.AdmNoticiasDto;
import mx.com.ga.cosmonaut.common.entity.administracion.noticias.AdmNoticias;
import mx.com.ga.cosmonaut.common.exception.ServiceException;

import java.text.ParseException;

public interface AdmNoticiaService {
    public RespuestaGenerica agregarNoticia(AdmNoticiasDto noticia) throws ServiceException;
    public RespuestaGenerica modificarNoticia(AdmNoticiasDto noticia) throws ServiceException;
    public RespuestaGenerica eliminarNoticia(Integer idNoticia);
    public RespuestaGenerica getNoticias(Integer clienteId,Integer clienteIdPadre,Integer personaId,String zonaHoraria) throws ServiceException, ParseException;
    public RespuestaGenerica getListaCategorias();
    public RespuestaGenerica getNoticiaDetalle(Integer idnoticia) throws ServiceException;
    public RespuestaGenerica getNoticiasCosmonaut() throws ServiceException;
    public RespuestaGenerica getNoticiasCliente(Integer clienteId) throws ServiceException;
    public RespuestaGenerica getNoticiasEmpresa(Integer clienteId) throws ServiceException;
}
