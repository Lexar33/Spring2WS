package spring2ws.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import spring2ws.dto.VwPersonalDto;
import spring2ws.util.Constantes;
import spring2ws.util.EAIUtil;
import spring2ws.util.Propiedades;
import spring2ws.util.AuditUtil;

import javax.validation.constraints.Null;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;

@Component
public class PersonalWSImpl implements  PersonalWS{

    Logger logger = LoggerFactory.getLogger(PersonalWSImpl.class);

    @Autowired
    private Propiedades propiedades;

    @Override
    public List<VwPersonalDto> listarPersonalFechaHasta(String idpersona, String idpersonal, String iddependencias, String idttipocontrato, String idtestado, String documentoidentidad, String fechahasta, String tipoconsulta) {

        logger.info("::::::::::::::::::: INICIAR listarPersonalFechaHasta :::::::::::::::::::::");

        List<VwPersonalDto> listFinal = new ArrayList<>();

        //String url = propiedades.SERVICE_HOST + propiedades.SERVICE_PERSONAL_WS + "listarPersonalFechaHasta?" + "idpersona=" + idpersona + "&idpersonal=" + idpersonal + "&iddependencias=" + iddependencias + "&idttipocontrato=" + idttipocontrato + "&idtestado=" + idtestado + "&documentoidentidad=" + documentoidentidad + "&fechahasta=" + fechahasta + "&tipoconsulta=" + tipoconsulta;

        String url = "https://s1.gestion.pnsu.gob.pe/personalws/" + "listarPersonalFechaHasta?" + "idpersona=" + idpersona + "&idpersonal=" + idpersonal + "&iddependencias=" + iddependencias + "&idttipocontrato=" + idttipocontrato + "&idtestado=" + idtestado + "&documentoidentidad=" + documentoidentidad + "&fechahasta=" + fechahasta + "&tipoconsulta=" + tipoconsulta;


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((ClientHttpRequestInterceptor) (HttpRequest request, byte[] body, ClientHttpRequestExecution execution) -> {
            //request.getHeaders().set(Constantes.HEADER_STRING, AuditUtil.getToken());
            String Token="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZHBlcnNvbmFcIjoxMjEsXCJub21icmVzXCI6XCJIT01FUiBNQU5BU0VTXCIsXCJhcGVsbGlkb3BhdGVybm9cIjpcIkxVQ0VST1wiLFwiYXBlbGxpZG9tYXRlcm5vXCI6XCJIVUFNQU5cIixcImRvY3VtZW50b2lkZW50aWRhZFwiOlwiNDUxNDUyNzFcIixcImlkcGVyc29uYWxcIjoxMjEsXCJjb3JyZW9pbnN0aXR1Y2lvbmFsXCI6XCJobHVjZXJvQHZpdmllbmRhLmdvYi5wZVwiLFwiZXNlbmNhcmdhZG9cIjowLFwiaWRjb250cmF0b1wiOjEyMSxcImlkZGVwZW5kZW5jaWFcIjo4LFwiaWRkZXBlbmRzdXBlcmlvclwiOjQsXCJkZXNjZGVwZW5kZW5jaWFcIjpcIkFyZWEgZGUgUmVjdXJzb3MgSHVtYW5vc1wiLFwiaWR1c3VhcmlvXCI6MTIxLFwiaWRzZWRlXCI6MSxcImRlc2NyaXBjaW9uc2VkZVwiOlwiQ0VOVFJBTFwiLFwiaXBjb25leGlvblwiOlwiMTAuMTY1LjcuMTlcIixcImxpc3RWd0NvbnRyYXRvUGVyZmlsRHRvXCI6W3tcImlkY29udHJhdG9cIjoxMjEsXCJpZGRlcGVuZGVuY2lhXCI6OCxcImlkdXN1YXJpb1wiOjEyMSxcIm5vbWJyZXVzdWFyaW9cIjpcIjQ1MTQ1MjcxXCIsXCJpZHVzdWFyaW9wZXJmaWxcIjoxLFwiaWRwZXJmaWxcIjozLFwiZGVub21pbmFjaW9uXCI6XCJQZXJzb25hbCBSUkhIXCJ9XX0iLCJzY29wZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfQURNSU4ifV0sImlzcyI6InF1aXF1ZSIsImlhdCI6MTcxODAxNzI5MiwiZXhwIjoxNzE4MDM1MjkyfQ.uhaY_ZY5_9Bsrfw0MNdI3HBF7nc0aJMDZQTWak0Wgvk";
            request.getHeaders().set(Constantes.HEADER_STRING, Token);
            return execution.execute(request, body);
        });

        Map<String, Object> getRest = restTemplate.getForObject(url, Map.class);

        logger.info("Sattus: " + getRest.get("Status"));

        if (Constantes.CODIGO_RPSTA_OK.equals(getRest.get("Status"))) {

            Object obj = getRest.get("Result");
            //List<?> list = new ArrayList<>();
            List<LinkedHashMap> list;

            if (obj.getClass().isArray()) {
                list = Arrays.asList((LinkedHashMap[]) obj);

                Iterator iterator = list.listIterator();

                while (iterator.hasNext()) {

                    LinkedHashMap lhm = (LinkedHashMap) iterator.next();

                    VwPersonalDto temp = new VwPersonalDto();

                    try {

                        temp.setIdpersona((int) lhm.get("idpersona"));
                        temp.setIdpersonal((int) lhm.get("idpersonal"));
                        temp.setNombres((String) lhm.get("nombres"));
                        temp.setApellidopaterno((String) lhm.get("apellidopaterno"));
                        temp.setApellidomaterno((String) lhm.get("apellidomaterno"));
                        temp.setDocumentoidentidad((String) lhm.get("documentoidentidad"));
                        temp.setUnidad((String) lhm.get("unidad"));
                        temp.setAreaequipo((String) lhm.get("areaequipo"));
                        temp.setFechainicio((String) lhm.get("fechainicio"));
                        if (Optional.ofNullable(lhm.get("fechainicio")).isPresent()) {
                            temp.setDfechainiciocontrato(EAIUtil.formatoFechaOut.parse((String) lhm.get("fechainicio")));
                        }
                        temp.setFechafin((String) lhm.get("fechafin"));
                        if (Optional.ofNullable(lhm.get("fechafin")).isPresent()) {
                            temp.setDfechafincontrato(EAIUtil.formatoFechaOut.parse((String) lhm.get("fechafin")));
                        }
                        temp.setFechacese((String) lhm.get("fechacese"));
                        if (Optional.ofNullable(lhm.get("fechacese")).isPresent()) {
                            temp.setDfechacesecontrato(EAIUtil.formatoFechaOut.parse((String) lhm.get("fechacese")));
                        }
                        temp.setNombrecargo((String) lhm.get("nombrecargo"));
                        temp.setTipocontrato((String) lhm.get("tipocontrato"));
                        temp.setFechafinadenda((String) lhm.get("fechafinadenda"));
                        if (Optional.ofNullable(lhm.get("fechafinadenda")).isPresent()) {
                            temp.setDfechafinadenda(EAIUtil.formatoFechaOut.parse((String) lhm.get("fechafinadenda")));
                        }
                        temp.setFechabajaadenda((String) lhm.get("fechabajaadenda"));
                        if (Optional.ofNullable(lhm.get("fechabajaadenda")).isPresent()) {
                            temp.setDfechabajaadenda(EAIUtil.formatoFechaOut.parse((String) lhm.get("fechabajaadenda")));
                        }
                        temp.setExoneradomarcar(Optional.ofNullable(lhm.get("exoneradomarcar")).isPresent() ? (int) lhm.get("exoneradomarcar") : 1);
                        temp.setHorarioingreso(Optional.ofNullable(lhm.get("horarioingreso")).isPresent()
                                ? EAIUtil.formatoHoraOut.parse((String) lhm.get("horarioingreso"))
                                : EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_INGRESO));
                        temp.setHorariosalida(Optional.ofNullable(lhm.get("horariosalida")).isPresent()
                                ? EAIUtil.formatoHoraOut.parse((String) lhm.get("horariosalida"))
                                : EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_SALIDA));
                        temp.setIddependenciadesignado(Optional.ofNullable(lhm.get("iddependenciadesignado")).isPresent() ? (String) lhm.get("iddependenciadesignado") : null);

                    } catch (ParseException ex) {
                        java.util.logging.Logger.getLogger(PersonalWSImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    listFinal.add(temp);
                }

            } else if (obj instanceof Collection) {
                list = new ArrayList<>((Collection<LinkedHashMap>) obj);

                Iterator iterator = list.listIterator();

                while (iterator.hasNext()) {

                    LinkedHashMap lhm = (LinkedHashMap) iterator.next();

                    VwPersonalDto temp = new VwPersonalDto();

                    try {

                        temp.setIdpersona((int) lhm.get("idpersona"));
                        temp.setIdpersonal((int) lhm.get("idpersonal"));
                        temp.setNombres((String) lhm.get("nombres"));
                        temp.setApellidopaterno((String) lhm.get("apellidopaterno"));
                        temp.setApellidomaterno((String) lhm.get("apellidomaterno"));
                        temp.setDocumentoidentidad((String) lhm.get("documentoidentidad"));
                        temp.setUnidad((String) lhm.get("unidad"));
                        temp.setAreaequipo((String) lhm.get("areaequipo"));
                        temp.setFechainicio((String) lhm.get("fechainicio"));
                        if (Optional.ofNullable(lhm.get("fechainicio")).isPresent()) {
                            temp.setDfechainiciocontrato(EAIUtil.formatoFechaOut.parse((String) lhm.get("fechainicio")));
                        }
                        temp.setFechafin((String) lhm.get("fechafin"));
                        if (Optional.ofNullable(lhm.get("fechafin")).isPresent()) {
                            temp.setDfechafincontrato(EAIUtil.formatoFechaOut.parse((String) lhm.get("fechafin")));
                        }
                        temp.setFechacese((String) lhm.get("fechacese"));
                        if (Optional.ofNullable(lhm.get("fechacese")).isPresent()) {
                            temp.setDfechacesecontrato(EAIUtil.formatoFechaOut.parse((String) lhm.get("fechacese")));
                        }
                        temp.setNombrecargo((String) lhm.get("nombrecargo"));
                        temp.setTipocontrato((String) lhm.get("tipocontrato"));
                        temp.setFechafinadenda((String) lhm.get("fechafinadenda"));
                        if (Optional.ofNullable(lhm.get("fechafinadenda")).isPresent()) {
                            temp.setDfechafinadenda(EAIUtil.formatoFechaOut.parse((String) lhm.get("fechafinadenda")));
                        }
                        temp.setFechabajaadenda((String) lhm.get("fechabajaadenda"));
                        if (Optional.ofNullable(lhm.get("fechabajaadenda")).isPresent()) {
                            temp.setDfechabajaadenda(EAIUtil.formatoFechaOut.parse((String) lhm.get("fechabajaadenda")));
                        }
                        temp.setExoneradomarcar(Optional.ofNullable(lhm.get("exoneradomarcar")).isPresent() ? (int) lhm.get("exoneradomarcar") : 1);
                        temp.setHorarioingreso(Optional.ofNullable(lhm.get("horarioingreso")).isPresent()
                                ? EAIUtil.formatoHoraOut.parse((String) lhm.get("horarioingreso"))
                                : EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_INGRESO));
                        temp.setHorariosalida(Optional.ofNullable(lhm.get("horariosalida")).isPresent()
                                ? EAIUtil.formatoHoraOut.parse((String) lhm.get("horariosalida"))
                                : EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_SALIDA));
                        temp.setIddependenciadesignado(Optional.ofNullable(lhm.get("iddependenciadesignado")).isPresent() ? (String) lhm.get("iddependenciadesignado") : null);

                    } catch (ParseException ex) {
                        java.util.logging.Logger.getLogger(PersonalWSImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    listFinal.add(temp);
                }
            }
        }

        logger.info("::::::::::::::::::::: FIN listarPersonalFechaHasta :::::::::::::::::::::::");

        return listFinal;
    }
}
