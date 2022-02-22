package mx.com.ga.cosmonaut.admin.services.impl;

import mx.com.ga.cosmonaut.admin.services.ModuloService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmCatModulos;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmCatSubmodulo;
import mx.com.ga.cosmonaut.common.entity.administracion.usuarios.AdmUsuarios;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmCatModulosRepository;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmCatSubmoduloRepository;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmUsuariosRepository;
import mx.com.ga.cosmonaut.common.repository.administracion.usuarios.AdmVersionCosmonautXsubmoduloRepository;
import mx.com.ga.cosmonaut.common.util.Constantes;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class ModuloServiceImpl implements ModuloService {

    @Inject
    private AdmCatModulosRepository admCatModulosRepository;

    @Inject
    private AdmCatSubmoduloRepository admCatSubmoduloRepository;

    @Inject
    private AdmVersionCosmonautXsubmoduloRepository admVersionCosmonautXsubmoduloRepository;

    @Inject
    private AdmUsuariosRepository admUsuariosRepository;

    @Override
    public RespuestaGenerica findByEsActivo(Boolean activo) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            respuestaGenerica.setDatos(admCatModulosRepository.findByEsActivoOrderBySecuencia(activo));
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            respuestaGenerica.setMensaje(Constantes.EXITO);
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" findByEsActivo" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

    @Override
    public RespuestaGenerica findByVersion(Integer id, Boolean activo) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        try {
            Set<AdmCatSubmodulo> submodulos = admVersionCosmonautXsubmoduloRepository.
                    findSubmoduloIdByVersionCosmonautIdVersionCosmonautIdAndVersionCosmonautIdEsActivo(id, activo);
            Set<AdmCatModulos> modulos = new TreeSet<AdmCatModulos>(//Se agrega funcion de comparacion para ordenar los modulos del menu
                    new Comparator<AdmCatModulos>() {
                        public int compare(AdmCatModulos s1, AdmCatModulos s2) {
                            int i = s1.getSecuencia() - s2.getSecuencia();
                            return i;
                        }
                    }
            );

            for (AdmCatSubmodulo submodulo : submodulos) {
                Optional<AdmCatModulos> modulo = admCatModulosRepository.findById(submodulo.getModuloId().getModuloId());
                modulo.ifPresent(modulos::add);
            }
            for (AdmCatModulos modulo : modulos) {
                modulo.setSubmodulos(new TreeSet<AdmCatSubmodulo>(
                        new Comparator<AdmCatSubmodulo>() {//SE agrega funcion de comparacion para agregar los submenus de cada modulo
                            public int compare(AdmCatSubmodulo s1, AdmCatSubmodulo s2) {
                                int i=0;
                                try {
                                    if(s1.getSubmoduloId()==s2.getSubmoduloId())//Si viene el mismo numero de id submodulo se considera un elemento repetido pero se agrega al final de la lista
                                        return -1;
                                    i = s1.getSecuencia() - s2.getSecuencia();
                                    if(i==0)//Si dos secuencias son iguales pero los numeros de id son diferentes se agrega por que se puede tratar de un submodulo de un modulo diferente
                                        return -1;
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                return i;
                            }
                        }
                ));
                for (AdmCatSubmodulo submodulo : submodulos) {
                    if (submodulo.getModuloId().getModuloId().equals(modulo.getModuloId())) {
                        modulo.getSubmodulos().add(admCatSubmoduloRepository
                                .findBySubmoduloId(submodulo.getSubmoduloId()));
                    }
                }
            }

            respuestaGenerica.setDatos(modulos);
            respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
            respuestaGenerica.setMensaje(Constantes.EXITO);
        }catch (Exception e){
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO +" findByEsActivo" + Constantes.ERROR_EXCEPCION, e);
        }
        return respuestaGenerica;
    }

    @Override
    public RespuestaGenerica findURL(Integer idUsuario, Integer idCliente, Integer version) throws ServiceException {
        RespuestaGenerica respuestaGenerica = new RespuestaGenerica();
        String url="https://datastudio.google.com/embed/reporting/a8fe9489-6dd3-425b-a96f-f2866830ddcc/page/y2hhC?params=%7B%22ds0.centro_cliente_padre%22%3A"+idCliente+"%7D";
        Optional<AdmUsuarios> user=admUsuariosRepository.findById(idUsuario);
        Integer rol=user.get().getRolId().getRolId();
        if (version==1)
            url="https://datastudio.google.com/embed/reporting/d60a5a01-b359-4963-82af-e67370d81203/page/odxgC";
        else
            if(rol==1)
                url="https://datastudio.google.com/embed/reporting/05746643-0280-4296-b289-165a889378c4/page/LsBhC?params=%7B%22ds9.centro_cliente_padre%22%3A%22"+idCliente+"%22%2C%22ds0.centro_cliente_padre%22%3A%22"+idCliente+"%22%7D";
        respuestaGenerica.setDatos(url);
        respuestaGenerica.setResultado(Constantes.RESULTADO_EXITO);
        respuestaGenerica.setMensaje(Constantes.EXITO);
        return respuestaGenerica;
    }

}
