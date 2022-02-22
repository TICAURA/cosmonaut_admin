package mx.com.ga.cosmonaut.admin.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.MediaType;
import lombok.extern.slf4j.Slf4j;
import mx.com.ga.cosmonaut.admin.services.ComprobanteMailService;
import mx.com.ga.cosmonaut.common.dto.RespuestaGenerica;
import mx.com.ga.cosmonaut.common.dto.mail.MailAttachment;
import mx.com.ga.cosmonaut.common.dto.mail.MailObject;
import mx.com.ga.cosmonaut.common.dto.mail.MailResult;
import mx.com.ga.cosmonaut.common.dto.mail.SendGridMailConfig;
import mx.com.ga.cosmonaut.common.entity.DocumentosEmpleado;
import mx.com.ga.cosmonaut.common.entity.calculo.NcrBitacoraTimbrado;
import mx.com.ga.cosmonaut.common.entity.calculo.NcrTimbre;
import mx.com.ga.cosmonaut.common.entity.colaborador.NcoPersona;
import mx.com.ga.cosmonaut.common.exception.ServiceException;
import mx.com.ga.cosmonaut.common.repository.DocumentosEmpleadoRepository;
import mx.com.ga.cosmonaut.common.repository.calculo.NcrBitacoraTimbradoRepository;
import mx.com.ga.cosmonaut.common.repository.calculo.NcrTimbreRepository;
import mx.com.ga.cosmonaut.common.repository.colaborador.NcoPersonaRepository;
import mx.com.ga.cosmonaut.common.service.DocumentosEmpleadoService;
import mx.com.ga.cosmonaut.common.service.MailService;
import mx.com.ga.cosmonaut.common.util.Constantes;
import mx.com.ga.cosmonaut.common.util.MailTemplateUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Singleton
public class ComprobanteMailServiceImpl implements ComprobanteMailService {

    @Inject
    private DocumentosEmpleadoService documentosEmpleadoService;

    @Inject
    private NcrBitacoraTimbradoRepository ncrBitacoraTimbradoRepository;

    @Inject
    private DocumentosEmpleadoRepository documentosEmpleadoRepository;

    @Inject
    private NcoPersonaRepository ncoPersonaRepository;
    @Inject
    private NcrTimbreRepository timbradorRepo;

    @Value("${mx.com.ga.cosmonaut.sendgrid.timbres-sender}")
    private String sender;

    @Value("${mx.com.ga.cosmonaut.sendgrid.apikey}")
    private String apikey;

    @Inject
    private MailService mailService;

    @Inject
    private MailTemplateUtil mailTemplateUtil;

    @Override
    public RespuestaGenerica sendMailComprobantes(Integer nominaXperiodoId) throws ServiceException {
        try {
            RespuestaGenerica respuesta = new RespuestaGenerica();

            Set<NcrBitacoraTimbrado> timbradosExitosos = ncrBitacoraTimbradoRepository
                    .findByNominaPeriodoIdAndExitoso(nominaXperiodoId);
            //HILO DE EJECUCION DE ENVIO
            new Thread(() -> envio(timbradosExitosos)).start();

            respuesta.setDatos(null);
            respuesta.setMensaje(Constantes.EXITO);
            respuesta.setResultado(Constantes.RESULTADO_EXITO);
            return respuesta;
        } catch (Exception e) {
            throw new ServiceException(Constantes.ERROR_CLASE + this.getClass().getSimpleName()
                    + Constantes.ERROR_METODO + " sendMailComprobantes " + Constantes.ERROR_EXCEPCION, e);
        }
    }

    private void envio(Set<NcrBitacoraTimbrado> timbradosExitosos) {
        mailService.setConfig(new SendGridMailConfig(apikey, sender));

        timbradosExitosos.forEach(timbrado -> {
            Optional<NcoPersona> persona = ncoPersonaRepository.findById(timbrado.getPersonaId());
            if (persona.isPresent()) {
                String archivoPdf64 = null;
                String archivoXml64 = null;

                Optional<DocumentosEmpleado> rowPdf = documentosEmpleadoRepository.findComprobanteFiscalNominaxPeriodoEmpleadoPDF(timbrado.getPersonaId(), timbrado.getClienteId(),timbrado.getNominaPeriodoId()+"_"+persona.get().getRfc());
                if (rowPdf.isPresent()) {
                    archivoPdf64 = getFile(rowPdf.get().getCmsArchivoId());
                }

               Optional<NcrTimbre> timbreOpt = this.timbradorRepo.findByNominaPeriodoIdAndCentrocClienteIdAndPersonaIdAndEsActual(timbrado.getNominaPeriodoId(),timbrado.getClienteId(),timbrado.getPersonaId(),true);
                if(timbreOpt.isPresent()){
                     NcrTimbre timbre = timbreOpt.get();
                     String xmllCapturado = timbre.getCfdi();
                     archivoXml64 = Base64.getEncoder().encodeToString(xmllCapturado.getBytes(StandardCharsets.UTF_8));
                }

                String nombreCompleto = new StringBuilder(persona.get().getNombre()).append(" ")
                        .append(persona.get().getApellidoPaterno()).append(" ")
                        .append(persona.get().getApellidoMaterno()!= null?persona.get().getApellidoMaterno():"")
                        .toString();
                MailResult mailResult = mailService.enviarCorreo(generarCfdiMail(nombreCompleto,
                        persona.get().getEmailCorporativo(), archivoPdf64, archivoXml64));
                log.info("Result: " + mailResult);
            }
        });
    }

    private String getFile(Integer fileId) {
        try {
            RespuestaGenerica respuestaDoc = documentosEmpleadoService.descargar(fileId);
            if (respuestaDoc.isResultado()) {
                ObjectMapper m = new ObjectMapper();
                Map<String, Object> mappedObject = m.convertValue(respuestaDoc.getDatos(), new TypeReference<>(){});
                return (String) mappedObject.get("contenido");
            } else {
                return null;
            }
        } catch (ServiceException ex) {
            return null;
        }
    }

    private MailObject generarCfdiMail(String nombreCompleto, String to, String pdf64, String xml64) {
        MailObject mailObject = new MailObject();
        mailObject.setTo(new String[]{to});
        mailObject.setSubject("Cosmonaut - Comprobante CFDI");

        if (pdf64 != null) {
            MailAttachment attachment = new MailAttachment();
            attachment.setFileName("CFDI.pdf");
            attachment.setMimeType(MediaType.APPLICATION_PDF);
            attachment.setContent(pdf64);
            mailObject.addAttachment(attachment);
        }

        if (xml64 != null) {
            MailAttachment attachment = new MailAttachment();
            attachment.setFileName("CFDISchema.xml");
            attachment.setMimeType(MediaType.APPLICATION_XML);
            attachment.setContent(xml64);
            mailObject.addAttachment(attachment);
        }

        Map<String, Object> valores = new HashMap<>();
        valores.put("nombre", nombreCompleto);
        mailObject.setHtmlContent(mailTemplateUtil.renderHtml("cfdiNomina", valores));

        return mailObject;
    }

}
