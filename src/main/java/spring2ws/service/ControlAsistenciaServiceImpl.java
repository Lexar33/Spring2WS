package spring2ws.service;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring2ws.dto.VwPersonalDto;
import spring2ws.exception.DBException;
import spring2ws.model.TControlAsistParcial;
import spring2ws.persist.ControlAsistenciaDao;
import spring2ws.proxy.PersonalWS;
import spring2ws.util.Constantes;
import spring2ws.util.EAIUtil;
import spring2ws.util.RestResponse;

import javax.naming.ldap.Control;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.IntFunction;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ControlAsistenciaServiceImpl implements ControlAsistenciaService {

    Logger logger = LoggerFactory.getLogger(ControlAsistenciaServiceImpl.class);

    @Autowired
    PersonalWS oPersonalWS;

    @Autowired
    ControlAsistenciaDao oReporteMarcacionDao;

    @Override
    public RestResponse registrarcontrolasistenciaparcial(String idpersonal,String documentoidentidad,String desde, String hasta) {
        logger.info("::::::::::::::::: INICIAR registrarcontrolasistenciaparcial :::::::::::::::");

        RestResponse response = new RestResponse();

        try {

            LocalDate lddesde = EAIUtil.formatoFechaOut.parse(desde).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ldhasta = EAIUtil.formatoFechaOut.parse(hasta).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            long numOfDaysBetween = ChronoUnit.DAYS.between(lddesde, ldhasta);

            logger.info("Cantidad de Dias {}", numOfDaysBetween + 1);
            //DEVUELVE LISTADO DE PERSONAL ACTIVO HASTA FECHA "DESDE"
            List<VwPersonalDto> listPersonal = oPersonalWS.listarPersonalFechaHasta(Constantes.VACIO, idpersonal, Constantes.VACIO, Constantes.VACIO, Constantes.VACIO, documentoidentidad, desde, Constantes.TIPO_CONSULTA_ALL);
            logger.info("Cantidad de listPersonal {}", listPersonal.size());

            List<TControlAsistParcial> listControlAsistenciaParcial = oReporteMarcacionDao.listControlAsistenciaParcial(idpersonal, documentoidentidad, desde, hasta);
            logger.info("Control de asistencia parcial {}", listControlAsistenciaParcial.size());
/*
            List<MarcacionPersonalDto> listMarcacionPersonal = oReporteMarcacionDao.listarMarcacionPersonalParametro(idpersonal, documentoidentidad, desde, hasta)
                    .stream().map(
                            o -> new MarcacionPersonalDto(
                                    (int) o[0], (String) o[1], (String) o[2], (String) o[3], (BigInteger) o[4], (String) o[5], (String) o[6], (Date) o[7], (Date) o[8], (String) o[9], (Date) o[10], (String) o[11], (Integer) o[12], (Integer) o[13], (Integer) o[14], ((BigInteger) o[15]).intValue(),
                                    (Date) o[16], (Date) o[17], (String) o[18], (String) o[19])
                    ).collect(Collectors.toList());

            logger.info("Cantidad de listMarcacionPersonal {}", listMarcacionPersonal.size());

            List<TFeriado> listFeriado = oReporteMarcacionDao.listarFeriado(desde, hasta);

            logger.info("Cantidad de listFeriado {}", listFeriado.size());

            List<DocumentoDto> listDocumentos = oReporteMarcacionDao.listarDocumentos(idpersonal, documentoidentidad, desde, hasta, Constantes.VACIO, Constantes.VACIO, Constantes.VACIO)
                    .stream().map(
                            o -> new DocumentoDto(
                                    (Integer) o[0], (String) o[1], (String) o[2], (String) o[3], (Integer) o[4], (String) o[5], (Integer) o[6], (Date) o[7], (Date) o[8], (Date) o[9], (Date) o[10], (Date) o[11], (Date) o[12], (Integer) o[13], (Integer) o[14], (String) o[15])
                    ).collect(Collectors.toList());

            logger.info("Cantidad de listDocumentos {}", listDocumentos.size());

            List<VacacionDto> listVacaciones = oReporteMarcacionDao.listarVacaciones(Constantes.VACIO, idpersonal, documentoidentidad, desde, hasta, Constantes.VACIO)
                    .stream().map(
                            o -> new VacacionDto(
                                    (Integer) o[0], (Integer) o[1], (Integer) o[2], (Integer) o[3], (Integer) o[4],
                                    (String) o[5], (String) o[6], (String) o[7], (String) o[8],
                                    Optional.ofNullable(o[9]).isPresent() ? (Date) o[9] : null,
                                    Optional.ofNullable(o[10]).isPresent() ? (Date) o[10] : null,
                                    Optional.ofNullable(o[11]).isPresent() ? (Date) o[11] : null,
                                    (String) o[12], (String) o[13], (String) o[14],
                                    Optional.ofNullable(o[15]).isPresent() ? (Date) o[15] : null,
                                    Optional.ofNullable(o[16]).isPresent() ? (Date) o[16] : null,
                                    (Double) o[17], (Double) o[18], (Double) o[19], (Double) o[20],
                                    (String) o[21], (String) o[22], (String) o[23], (String) o[24],
                                    (String) o[25], (String) o[26], (String) o[27])
                    ).collect(Collectors.toList());

            logger.info("Cantidad de listVacaciones {}", listVacaciones.size());

            IntStream.iterate(0, i -> i + 1)
                    .limit(numOfDaysBetween + 1)
                    .mapToObj((IntFunction<?>) i -> {

                        listPersonal.forEach((VwPersonalDto temp) -> {

                            try {

                                if (EAIUtil.formatoFechaOut.parse(temp.getFechainicio()).before(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                                        ? (Optional.ofNullable(temp.getDfechacesecontrato()).isPresent()
                                        ? temp.getDfechacesecontrato().after(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant())) || temp.getDfechacesecontrato().equals(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                                        : (Optional.ofNullable(temp.getDfechabajaadenda()).isPresent()
                                        ? temp.getDfechabajaadenda().after(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant())) || temp.getDfechabajaadenda().equals(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                                        : (Optional.ofNullable(temp.getDfechafinadenda()).isPresent()
                                        ? temp.getDfechafinadenda().after(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant())) || temp.getDfechafinadenda().equals(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                                        : (Optional.ofNullable(temp.getDfechafincontrato()).isPresent()
                                        ? temp.getDfechafincontrato().after(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant())) || temp.getDfechafincontrato().equals(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                                        : true))))
                                        : EAIUtil.formatoFechaOut.parse(temp.getFechainicio()).equals(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {

                                    List<MarcacionPersonalDto> listMarcacionPersonalFiltrado = listMarcacionPersonal.stream()
                                            .filter((MarcacionPersonalDto t)
                                                    -> EAIUtil.formatoFechaOut.format(t.getFecha()).equals(EAIUtil.formatoFechaOut.format(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()))) && t.getDocumentoidentidad().equals(temp.getDocumentoidentidad()))
                                            .collect(Collectors.toList());
                                    listMarcacionPersonal.removeAll(listMarcacionPersonalFiltrado);

                                    List<TControlAsistParcial> listControlAsistenciaParcialFiltrado = listControlAsistenciaParcial.stream()
                                            .filter((TControlAsistParcial t)
                                                    -> EAIUtil.formatoFechaOut.format(t.getFecha()).equals(EAIUtil.formatoFechaOut.format(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()))) && t.getIdpersonal().getIdpersonal().equals(temp.getIdpersonal()))
                                            .collect(Collectors.toList());
                                    listControlAsistenciaParcial.removeAll(listControlAsistenciaParcialFiltrado);

                                    JSONArray arrayDocumentosJSON = new JSONArray();

                                    int minutoscitacion1 = 0,
                                            minutoscitacion2 = 0,
                                            minutoscitacion3 = 0,
                                            minutoscompensacion1 = 0,
                                            minutoscompensacion2 = 0,
                                            minutoscompensacion3 = 0,
                                            minutoscapacitacionoficializada1 = 0,
                                            minutoscapacitacionoficializada2 = 0,
                                            minutoscapacitacionoficializada3 = 0,
                                            minutospermisocongoceotros1 = 0,
                                            minutospermisocongoceotros2 = 0,
                                            minutospermisocongoceotros3 = 0,
                                            minutoscitamedica1 = 0,
                                            minutoscitamedica2 = 0,
                                            minutoscitamedica3 = 0,
                                            minutoslimametropolitana1 = 0,
                                            minutoslimametropolitana2 = 0,
                                            minutoslimametropolitana3 = 0,
                                            minutoscapmediajornada1 = 0,
                                            minutoscapmediajornada2 = 0,
                                            minutoscapmediajornada3 = 0,
                                            minutosasuntosparticulares1 = 0,
                                            minutosasuntosparticulares2 = 0,
                                            minutosasuntosparticulares3 = 0,
                                            minutosremotoparcial1 = 0,
                                            minutosremotoparcial2 = 0,
                                            minutosremotoparcial3 = 0;

                                    int horasatrabajar = propiedades.VALUE_JORNADA_LABORAL_DIA,
                                            //horasrestante = 0,
                                            horastrabajadas = 0,
                                            horastrabajadaspresencial = 0,
                                            horastrabajadasremoto = 0,
                                            minutospermisocongoce = 0,
                                            minutospermisosingoce = 0,
                                            horaslicenciacongoce = 0,
                                            horaslicenciasingoce = 0,
                                            horascomisiondeservicio = 0,
                                            minutossalud = 0,
                                            horasvacaciones = 0,
                                            horascapacitacion = 0,
                                            minutoscompensados = 0;

                                    boolean diaregularizado = false, diasolicitado = false;

                                    List<DocumentoDto> listDocumentosFiltradoBetween = listDocumentos.stream()
                                            .filter((DocumentoDto tdoc)
                                                    -> Objects.equals(tdoc.getDocumentoidentidad(), temp.getDocumentoidentidad())
                                                    && isWithinRange(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()), tdoc.getFechainicio(), tdoc.getFechafin())
                                                    && !Objects.equals(tdoc.getIdtestadodocumento(), Constantes.CODIGO_ESTADO_DOCUMENTO_RECHAZADO_ANULADO)
                                            ).collect(Collectors.toList());

                                    for (DocumentoDto documentoDto : listDocumentosFiltradoBetween) {

                                        JSONObject documentoJSON = new JSONObject();
                                        documentoJSON.put("iddocumento", documentoDto.getIddocumento().toString());
                                        documentoJSON.put("idtasunto", documentoDto.getIdtasunto());
                                        documentoJSON.put("idtestadodocumento", documentoDto.getIdtestadodocumento());
                                        documentoJSON.put("tipodocumento", documentoDto.getTipodocumento());
                                        documentoJSON.put("estadodocumento", documentoDto.getEstadodocumento());
                                        documentoJSON.put("tiposolicitud", documentoDto.getTiposolicitud().replace("SOLICITUD ", ""));
                                        documentoJSON.put("asunto", documentoDto.getAsunto());
                                        documentoJSON.put("horainicio", documentoDto.getHorasalida());
                                        documentoJSON.put("horafin", documentoDto.getHoraretorno());
                                        documentoJSON.put("horas", Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                ? documentoDto.getHoras()
                                                : Optional.ofNullable(documentoDto.getHoraretorno()).isPresent() && Optional.ofNullable(documentoDto.getHorasalida()).isPresent()
                                                ? (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000))
                                                : horasatrabajar);
                                        documentoJSON.put("minutos", Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                ? documentoDto.getHoras() * 60
                                                : Optional.ofNullable(documentoDto.getHoraretorno()).isPresent() && Optional.ofNullable(documentoDto.getHorasalida()).isPresent()
                                                ? (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000)) * 60
                                                : horasatrabajar * 60);
                                        documentoJSON.put("fechainicio", Optional.ofNullable(documentoDto.getFechainicio()).isPresent() ? EAIUtil.formatoFechaOut.format(documentoDto.getFechainicio()) : null);
                                        documentoJSON.put("fechafin", Optional.ofNullable(documentoDto.getFechafin()).isPresent() ? EAIUtil.formatoFechaOut.format(documentoDto.getFechafin()) : null);
                                        documentoJSON.put("fechaocurrencia", Optional.ofNullable(documentoDto.getFechaocurrencia()).isPresent() ? EAIUtil.formatoFechaOut.format(documentoDto.getFechaocurrencia()) : null);
                                        arrayDocumentosJSON.put(documentoJSON);

                                        diasolicitado = true;

                                        if (Objects.equals(documentoDto.getIdtestadodocumento(), Constantes.CODIGO_ESTADO_DOCUMENTO_APROBADO)) {

                                            if (null != documentoDto.getIdtasunto()) {

                                                if (lddesde.plusDays(i).getDayOfWeek().getValue() == 6 || lddesde.plusDays(i).getDayOfWeek().getValue() == 7) {

                                                    if (documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_COMISION_SERVICIO_VIAJEINTERIORPAIS || documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_COMISION_SERVICIO_VIAJEEXTERIORPAIS) {
                                                        diaregularizado = true;
                                                        horastrabajadas = horastrabajadas >= propiedades.VALUE_JORNADA_LABORAL_DIA_MAXIMO ? horastrabajadas : horastrabajadas + horasatrabajar;
                                                        horascomisiondeservicio = horascomisiondeservicio >= propiedades.VALUE_JORNADA_LABORAL_DIA_MAXIMO ? horascomisiondeservicio : horascomisiondeservicio + horasatrabajar;
                                                    }
                                                } else {

                                                    if (documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_PERMISO_SALUD_DESCANSOMEDICO || documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_PERMISO_SALUD_SUBSIDIO) {

                                                        diaregularizado = true;
                                                        horastrabajadas = horastrabajadas >= horasatrabajar ? horastrabajadas : horastrabajadas + horasatrabajar;
                                                        minutossalud = minutossalud >= (propiedades.VALUE_JORNADA_LABORAL_DIA_MAXIMO * 60) ? minutossalud : minutossalud + (horasatrabajar * 60);

                                                    } else if (documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_COMISION_SERVICIO_VIAJEINTERIORPAIS || documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_COMISION_SERVICIO_VIAJEEXTERIORPAIS) {

                                                        diaregularizado = true;
                                                        horastrabajadas = horastrabajadas >= horasatrabajar ? horastrabajadas : horastrabajadas + horasatrabajar;
                                                        horascomisiondeservicio = horascomisiondeservicio >= propiedades.VALUE_JORNADA_LABORAL_DIA_MAXIMO ? horascomisiondeservicio : horascomisiondeservicio + horasatrabajar;

                                                    } else if (documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_LICENCIA_CON_GOCE_FALLECIMIENTOFAM
                                                            || documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_LICENCIA_CON_GOCE_MATERNIDAD
                                                            || documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_LICENCIA_CON_GOCE_PATERNIDAD
                                                            || documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_LICENCIA_CON_GOCE_ENFERMEDADFAM
                                                            || documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_LICENCIA_CON_GOCE_REHABILITACION
                                                            || documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_LICENCIA_CON_GOCE_EMERGENCIACOVID) {

                                                        diaregularizado = true;
                                                        horastrabajadas = horastrabajadas >= horasatrabajar ? horastrabajadas : horastrabajadas + horasatrabajar;
                                                        horaslicenciacongoce = horaslicenciacongoce >= propiedades.VALUE_JORNADA_LABORAL_DIA_MAXIMO ? horaslicenciacongoce : horaslicenciacongoce + horasatrabajar;

                                                    } else if (documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_LICENCIA_SIN_GOCE_REMUNERACIONES) {

                                                        diaregularizado = true;
                                                        horasatrabajar = 0;
                                                        horaslicenciasingoce = horaslicenciasingoce >= propiedades.VALUE_JORNADA_LABORAL_DIA_MAXIMO ? horaslicenciasingoce : horaslicenciasingoce + horasatrabajar;

                                                    } else if (documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_MEDIDACAUTELAR_MEDIDACAUTELAR) {
                                                        diaregularizado = true;
                                                        horastrabajadas = horastrabajadas >= horasatrabajar ? horastrabajadas : horastrabajadas + horasatrabajar;
                                                    } else if (documentoDto.getIdtasunto() == propiedades.VALUE_TIPO_SOL_PERMISO_CON_GOCE_LACTANCIA && Optional.ofNullable(documentoDto.getHoraretorno()).isPresent() && Optional.ofNullable(documentoDto.getHorasalida()).isPresent()) {
                                                        if (documentoDto.getHorasalida().getTime() <= EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_INICIO).getTime()) {
                                                            temp.setHorarioingreso(documentoDto.getHoraretorno());
                                                        } else {
                                                            temp.setHorariosalida(documentoDto.getHorasalida());
                                                        }
                                                    } else {
                                                        diaregularizado = true;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    List<DocumentoDto> listDocumentosFiltrado = listDocumentos.stream()
                                            .filter((DocumentoDto tdoc)
                                                    -> Objects.equals(tdoc.getDocumentoidentidad(), temp.getDocumentoidentidad())
                                                    && Objects.equals(tdoc.getFechaocurrencia(), Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                                                    && !Objects.equals(tdoc.getIdtestadodocumento(), Constantes.CODIGO_ESTADO_DOCUMENTO_RECHAZADO_ANULADO)
                                            ).collect(Collectors.toList());
                                    listDocumentos.removeAll(listDocumentosFiltrado);

                                    try {

                                        for (DocumentoDto documentoDto : listDocumentosFiltrado) {

                                            JSONObject documentoJSON = new JSONObject();
                                            documentoJSON.put("iddocumento", documentoDto.getIddocumento().toString());
                                            documentoJSON.put("idtasunto", documentoDto.getIdtasunto());
                                            documentoJSON.put("idtestadodocumento", documentoDto.getIdtestadodocumento());
                                            documentoJSON.put("tipodocumento", documentoDto.getTipodocumento());
                                            documentoJSON.put("estadodocumento", documentoDto.getEstadodocumento());
                                            documentoJSON.put("tiposolicitud", documentoDto.getTiposolicitud().replace("SOLICITUD ", ""));
                                            documentoJSON.put("asunto", documentoDto.getAsunto());
                                            documentoJSON.put("horainicio", documentoDto.getHorasalida());
                                            documentoJSON.put("horafin", documentoDto.getHoraretorno());
                                            documentoJSON.put("horas", Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                    ? documentoDto.getHoras()
                                                    : Optional.ofNullable(documentoDto.getHoraretorno()).isPresent() && Optional.ofNullable(documentoDto.getHorasalida()).isPresent()
                                                    ? (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000))
                                                    : horasatrabajar);
                                            documentoJSON.put("minutos", Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                    ? documentoDto.getHoras() * 60
                                                    : Optional.ofNullable(documentoDto.getHoraretorno()).isPresent() && Optional.ofNullable(documentoDto.getHorasalida()).isPresent()
                                                    ? (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000)) * 60
                                                    : horasatrabajar * 60);
                                            documentoJSON.put("fechainicio", Optional.ofNullable(documentoDto.getFechainicio()).isPresent() ? EAIUtil.formatoFechaOut.format(documentoDto.getFechainicio()) : null);
                                            documentoJSON.put("fechafin", Optional.ofNullable(documentoDto.getFechafin()).isPresent() ? EAIUtil.formatoFechaOut.format(documentoDto.getFechafin()) : null);
                                            documentoJSON.put("fechaocurrencia", Optional.ofNullable(documentoDto.getFechaocurrencia()).isPresent() ? EAIUtil.formatoFechaOut.format(documentoDto.getFechaocurrencia()) : null);
                                            arrayDocumentosJSON.put(documentoJSON);

                                            diasolicitado = true;

                                            if (Objects.equals(documentoDto.getIdtestadodocumento(), Constantes.CODIGO_ESTADO_DOCUMENTO_APROBADO)) {
                                                if (null != documentoDto.getIdtasunto()) {

                                                    if (documentoDto.getIdtasunto().equals(propiedades.VALUE_TIPO_SOL_PERMISO_CON_GOCE_ONOMASTICO)) {

                                                        horastrabajadas = horastrabajadas >= horasatrabajar ? horastrabajadas : horastrabajadas + horasatrabajar;
                                                        minutospermisocongoce = minutospermisocongoce + (horasatrabajar * 60);
                                                        diaregularizado = true;

                                                    } else if (documentoDto.getIdtasunto().equals(propiedades.VALUE_TIPO_SOL_PERMISO_CON_GOCE_CITACIONJUDICIAL)) {

                                                        if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() < temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                            minutoscitacion1 = minutoscitacion1 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHorasalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_LACTANCIA_FIN).getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                            minutoscitacion2 = minutoscitacion2 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                        } else {
                                                            minutoscitacion3 = minutoscitacion3 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        }
                                                        horastrabajadas = horastrabajadas + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras()
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 60 * 1000)));
                                                        minutospermisocongoce = minutospermisocongoce + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras() * 60
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000)));

                                                    } else if (documentoDto.getIdtasunto().equals(propiedades.VALUE_TIPO_SOL_PERMISO_CON_GOCE_CAPACITACIONOFIC)) {

                                                        if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() < temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                            minutoscapacitacionoficializada1 = minutoscapacitacionoficializada1 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHorasalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_LACTANCIA_FIN).getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                            minutoscapacitacionoficializada2 = minutoscapacitacionoficializada2 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                        } else {
                                                            minutoscapacitacionoficializada3 = minutoscapacitacionoficializada3 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        }
                                                        horastrabajadas = horastrabajadas + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras()
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 60 * 1000)));
                                                        minutospermisocongoce = minutospermisocongoce + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras() * 60
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000)));

                                                    } else if (documentoDto.getIdtasunto().equals(propiedades.VALUE_TIPO_SOL_PERMISO_CON_GOCE_OTROS)) {

                                                        if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() < temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                            minutospermisocongoceotros1 = minutospermisocongoceotros1 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHorasalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_LACTANCIA_FIN).getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                            minutospermisocongoceotros2 = minutospermisocongoceotros2 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                        } else {
                                                            minutospermisocongoceotros3 = minutospermisocongoceotros3 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        }
                                                        horastrabajadas = horastrabajadas + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras()
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 60 * 1000)));
                                                        minutospermisocongoce = minutospermisocongoce + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras() * 60
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000)));

                                                    } else if (documentoDto.getIdtasunto().equals(propiedades.VALUE_TIPO_SOL_PERMISO_SIN_GOCE_ASUNTOSPARTICULARES)) {

                                                        if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() < temp.getHorariosalida().getTime()) {
                                                            minutosasuntosparticulares1 = minutosasuntosparticulares1 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHorasalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_LACTANCIA_FIN).getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            minutosasuntosparticulares2 = minutosasuntosparticulares2 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                        } else {
                                                            minutosasuntosparticulares3 = minutosasuntosparticulares3 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        }
//                                                        horastrabajadas = horastrabajadas + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
//                                                                ? documentoDto.getHoras()
//                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 60 * 1000)));
                                                        minutospermisosingoce = minutospermisosingoce + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras() * 60
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 60 * 1000)));

                                                    } else if (documentoDto.getIdtasunto().equals(propiedades.VALUE_TIPO_SOL_PERMISO_SALUD_CITAMEDICA)) {

                                                        if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() < temp.getHorariosalida().getTime()) {
                                                            minutoscitamedica1 = minutoscitamedica1 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHorasalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_LACTANCIA_FIN).getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            minutoscitamedica2 = minutoscitamedica2 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                        } else {
                                                            minutoscitamedica3 = minutoscitamedica3 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        }
                                                        horastrabajadas = horastrabajadas + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras()
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 60 * 1000)));
                                                        minutossalud = minutossalud + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras() * 60
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000)));

                                                    } else if (documentoDto.getIdtasunto().equals(propiedades.VALUE_TIPO_SOL_COMISION_SERVICIO_LIMAMETROPOLITANA)) {

                                                        if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() < temp.getHorariosalida().getTime()) {
                                                            minutoslimametropolitana1 = minutoslimametropolitana1 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHorasalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_LACTANCIA_FIN).getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            minutoslimametropolitana2 = minutoslimametropolitana2 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                        } else {
                                                            minutoslimametropolitana3 = minutoslimametropolitana3 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        }
                                                        horastrabajadas = horastrabajadas + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras()
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 60 * 1000)));
                                                        horascomisiondeservicio = horascomisiondeservicio + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras() * 60
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 60 * 1000)));

                                                    } else if (documentoDto.getIdtasunto().equals(propiedades.VALUE_TIPO_SOL_COMPENSCION_SOLICITUDCOMPENSACION)) {

                                                        if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() < temp.getHorariosalida().getTime()) {
                                                            minutoscompensacion1 = minutoscompensacion1 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() > temp.getHorarioingreso().getTime() && documentoDto.getHorasalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_LACTANCIA_FIN).getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            minutoscompensacion2 = minutoscompensacion2 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (documentoDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime() && documentoDto.getHoraretorno().getTime() >= temp.getHorariosalida().getTime()) {
                                                            diaregularizado = true;
                                                        } else {
                                                            minutoscompensacion3 = minutoscompensacion3 + (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000));
                                                        }
                                                        horastrabajadas = horastrabajadas + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras()
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 60 * 1000)));
                                                        minutoscompensados = minutoscompensados + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras() * 60
                                                                : (int) ((documentoDto.getHoraretorno().getTime() - documentoDto.getHorasalida().getTime()) / (60 * 1000)));

                                                    } else if (documentoDto.getIdtasunto().equals(propiedades.VALUE_TIPO_SOL_TRABAJO_REMOTO_TRABAJOREMOTO)) {

                                                        diaregularizado = true;
                                                        horastrabajadas = horastrabajadas + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras()
                                                                : 0);
                                                        horastrabajadasremoto = horastrabajadasremoto + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras()
                                                                : 0);
                                                    } else if (documentoDto.getIdtasunto().equals(propiedades.VALUE_TIPO_SOL_TELETRABAJO_TELETRABAJO)) {
                                                        diaregularizado = true;
                                                        horastrabajadasremoto = horastrabajadasremoto + (Optional.ofNullable(documentoDto.getHoras()).isPresent()
                                                                ? documentoDto.getHoras()
                                                                : 0);
                                                    } else if (documentoDto.getIdtasunto().equals(propiedades.VALUE_TIPO_SOL_PERMISO_COMPENSABLE_FERIADOCOMP)) {

                                                        horastrabajadas = horastrabajadas + (Optional.ofNullable(documentoDto.getHoras()).isPresent() ? documentoDto.getHoras() : horasatrabajar);
                                                        diaregularizado = true;
                                                    }
                                                }
                                            }
                                        }

                                        List<VacacionDto> listVacacionesFiltradoBetween = listVacaciones.stream()
                                                .filter((VacacionDto tvac)
                                                        -> Objects.equals(tvac.getDocumentoidentidad(), temp.getDocumentoidentidad()) && isWithinRange(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()), tvac.getFechainicio(), tvac.getFechafin())
                                                ).collect(Collectors.toList());

                                        for (VacacionDto vacacionDto : listVacacionesFiltradoBetween) {

                                            JSONObject documentoJSON = new JSONObject();
                                            documentoJSON.put("idvacacion", vacacionDto.getIdvacacion().toString());
                                            documentoJSON.put("idttipovacacion", vacacionDto.getIdttipovacacion());
                                            documentoJSON.put("idtestadovacacion", vacacionDto.getIdtestadovacacion());
                                            documentoJSON.put("tipovacacion", vacacionDto.getTipovacacion());
                                            documentoJSON.put("estadovacacion", vacacionDto.getEstadovacacion());
                                            documentoJSON.put("periodo", vacacionDto.getPeriodo());
                                            documentoJSON.put("diastomada", vacacionDto.getDiastomada());
                                            documentoJSON.put("horastomada", (vacacionDto.getDiastomada() * horasatrabajar));
                                            documentoJSON.put("fechainicio", Optional.ofNullable(vacacionDto.getFechainicio()).isPresent() ? EAIUtil.formatoFechaOut.format(vacacionDto.getFechainicio()) : null);
                                            documentoJSON.put("fechafin", Optional.ofNullable(vacacionDto.getFechafin()).isPresent() ? EAIUtil.formatoFechaOut.format(vacacionDto.getFechafin()) : null);
                                            documentoJSON.put("fechaocurrencia", Optional.ofNullable(vacacionDto.getFechaocurrencia()).isPresent() ? EAIUtil.formatoFechaOut.format(vacacionDto.getFechaocurrencia()) : null);
                                            arrayDocumentosJSON.put(documentoJSON);

                                            diasolicitado = true;

                                            if (Objects.equals(vacacionDto.getIdtestadovacacion(), propiedades.VALUE_TIPO_ESTADO_VAC_GOZADO)) {

                                                if (null != vacacionDto.getIdttipovacacion()) {

                                                    if (vacacionDto.getIdttipovacacion() == propiedades.VALUE_TIPO_SOL_VACACIONES_MEDIAJORNADA) {

                                                        if (vacacionDto.getHorasalida().getTime() <= temp.getHorarioingreso().getTime()) {
                                                            minutoscapmediajornada1 = minutoscapmediajornada1 + (int) ((vacacionDto.getHoraretorno().getTime() - vacacionDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else if (vacacionDto.getHorasalida().getTime() >= EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_INICIO).getTime()) {
                                                            minutoscapmediajornada2 = minutoscapmediajornada2 + (int) ((vacacionDto.getHoraretorno().getTime() - vacacionDto.getHorasalida().getTime()) / (60 * 1000));
                                                        } else {
                                                            minutoscapmediajornada3 = minutoscapmediajornada3 + (int) ((vacacionDto.getHoraretorno().getTime() - vacacionDto.getHorasalida().getTime()) / (60 * 1000));
                                                        }

                                                        horastrabajadas = horastrabajadas >= propiedades.VALUE_JORNADA_LABORAL_DIA_MAXIMO ? horastrabajadas : horastrabajadas + (horasatrabajar / 2);
                                                        horasvacaciones = horasvacaciones >= horasatrabajar ? horasvacaciones : horasvacaciones + (horasatrabajar / 2);

                                                    } else if (vacacionDto.getIdttipovacacion() == propiedades.VALUE_TIPO_SOL_VACACIONES_ADELANTO || vacacionDto.getIdttipovacacion() == propiedades.VALUE_TIPO_SOL_VACACIONES_NORMAL || vacacionDto.getIdttipovacacion() == propiedades.VALUE_TIPO_SOL_VACACIONES_FRACCIONADO) {

                                                        horastrabajadas = horastrabajadas >= horasatrabajar ? horastrabajadas : horastrabajadas + horasatrabajar;
                                                        horasvacaciones = horasvacaciones >= horasatrabajar ? horasvacaciones : horasvacaciones + horasatrabajar;
                                                    }

                                                    diaregularizado = true;
                                                }
                                            }
                                        }
                                    } catch (ParseException | JSONException ex) {
                                        java.util.logging.Logger.getLogger(ControlAsistenciaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    if (!listMarcacionPersonalFiltrado.isEmpty()) {

                                        try {

                                            MarcacionPersonalDto dto = new MarcacionPersonalDto();
                                            PropertyUtils.copyProperties(dto, listMarcacionPersonalFiltrado.get(0));
                                            dto.setHorarioingreso(temp.getHorarioingreso());
                                            dto.setHorariosalida(temp.getHorariosalida());

                                            if (EAIUtil.formatoFechaOut.parse(listMarcacionPersonalFiltrado.get(0).getSfecha()).getDay() == 6 || EAIUtil.formatoFechaOut.parse(listMarcacionPersonalFiltrado.get(0).getSfecha()).getDay() == 0) {

                                                dto.setHorasacompensar(
                                                        Optional.ofNullable(listMarcacionPersonalFiltrado.get(0).getIngreso()).isPresent() && Optional.ofNullable(listMarcacionPersonalFiltrado.get(0).getSalida()).isPresent()
                                                                ? (int) ((listMarcacionPersonalFiltrado.get(0).getSalida().getTime() - listMarcacionPersonalFiltrado.get(0).getIngreso().getTime()) / (60 * 60 * 1000))
                                                                : 0);
                                            } else if (!diaregularizado) {
                                                dto.setHorasacompensar(
                                                        Optional.ofNullable(listMarcacionPersonalFiltrado.get(0).getMinutoextrasalida()).isPresent()
                                                                ? (listMarcacionPersonalFiltrado.get(0).getMinutoextrasalida() / 60)
                                                                : 0);
                                            }

                                            if (!diaregularizado) {

                                                if (minutoscompensacion1 > 0) {
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutoscompensacion1) < 0 ? 0 : (dto.getMinutotardanza() - minutoscompensacion1));
                                                }
                                                if (minutoscompensacion2 > 0) {
                                                    if (dto.getSalida() != null ? dto.getSalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() : dto.getSalida() != null) {
                                                        Long descuentorefrigerio = (EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() - dto.getSalida().getTime()) / (60 * 1000);
                                                        minutoscompensacion2 = (int) (minutoscompensacion2 + (descuentorefrigerio <= 60 ? descuentorefrigerio : 60));
                                                    }
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutoscompensacion2) < 0 ? 0 : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutoscompensacion2));
                                                }
                                                if (minutoscompensacion3 > 0) {
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutoscompensacion3 < 0 ? dto.getMinutotardanza() - minutoscompensacion3 : 0)) < 0
                                                            ? 0
                                                            : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutoscompensacion3 < 0 ? dto.getMinutotardanza() - minutoscompensacion3 : 0)));
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutoscompensacion3) < 0 ? 0 : (dto.getMinutotardanza() - minutoscompensacion3));
                                                }

                                                if (minutoscitacion1 > 0) {
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutoscitacion1) < 0 ? 0 : (dto.getMinutotardanza() - minutoscitacion1));
                                                }
                                                if (minutoscitacion2 > 0) {
                                                    if (dto.getSalida() != null ? dto.getSalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() : dto.getSalida() != null) {
                                                        Long descuentorefrigerio = (EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() - dto.getSalida().getTime()) / (60 * 1000);
                                                        minutoscitacion2 = (int) (minutoscitacion2 + (descuentorefrigerio <= 60 ? descuentorefrigerio : 60));
                                                    }
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutoscitacion2) < 0 ? 0 : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutoscitacion2));
                                                }
                                                if (minutoscitacion3 > 0) {
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutoscitacion3 < 0 ? dto.getMinutotardanza() - minutoscitacion3 : 0)) < 0
                                                            ? 0
                                                            : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutoscitacion3 < 0 ? dto.getMinutotardanza() - minutoscitacion3 : 0)));
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutoscitacion3) < 0 ? 0 : (dto.getMinutotardanza() - minutoscitacion3));
                                                }

                                                if (minutoscapacitacionoficializada1 > 0) {
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutoscapacitacionoficializada1) < 0 ? 0 : (dto.getMinutotardanza() - minutoscapacitacionoficializada1));
                                                }
                                                if (minutoscapacitacionoficializada2 > 0) {
                                                    if (dto.getSalida() != null ? dto.getSalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() : dto.getSalida() != null) {
                                                        Long descuentorefrigerio = (EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() - dto.getSalida().getTime()) / (60 * 1000);
                                                        minutoscapacitacionoficializada2 = (int) (minutoscapacitacionoficializada2 + (descuentorefrigerio <= 60 ? descuentorefrigerio : 60));
                                                    }
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutoscapacitacionoficializada2) < 0 ? 0 : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutoscapacitacionoficializada2));
                                                }
                                                if (minutoscapacitacionoficializada3 > 0) {
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutoscapacitacionoficializada3 < 0 ? dto.getMinutotardanza() - minutoscapacitacionoficializada3 : 0)) < 0
                                                            ? 0
                                                            : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutoscapacitacionoficializada3 < 0 ? dto.getMinutotardanza() - minutoscapacitacionoficializada3 : 0)));
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutoscapacitacionoficializada3) < 0 ? 0 : (dto.getMinutotardanza() - minutoscapacitacionoficializada3));
                                                }

                                                if (minutospermisocongoceotros1 > 0) {
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutospermisocongoceotros1) < 0 ? 0 : (dto.getMinutotardanza() - minutospermisocongoceotros1));
                                                }
                                                if (minutospermisocongoceotros2 > 0) {
                                                    if (dto.getSalida() != null ? dto.getSalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() : dto.getSalida() != null) {
                                                        Long descuentorefrigerio = (EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() - dto.getSalida().getTime()) / (60 * 1000);
                                                        minutospermisocongoceotros2 = (int) (minutospermisocongoceotros2 + (descuentorefrigerio <= 60 ? descuentorefrigerio : 60));
                                                    }
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutospermisocongoceotros2) < 0 ? 0 : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutospermisocongoceotros2));
                                                }
                                                if (minutospermisocongoceotros3 > 0) {
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutospermisocongoceotros3 < 0 ? dto.getMinutotardanza() - minutospermisocongoceotros3 : 0)) < 0
                                                            ? 0
                                                            : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutospermisocongoceotros3 < 0 ? dto.getMinutotardanza() - minutospermisocongoceotros3 : 0)));
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutospermisocongoceotros3) < 0 ? 0 : (dto.getMinutotardanza() - minutospermisocongoceotros3));
                                                }

                                                if (minutoscitamedica1 > 0) {
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutoscitamedica1) < 0 ? 0 : (dto.getMinutotardanza() - minutoscitamedica1));
                                                }
                                                if (minutoscitamedica2 > 0) {
                                                    if (dto.getSalida() != null ? dto.getSalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() : dto.getSalida() != null) {
                                                        Long descuentorefrigerio = (EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() - dto.getSalida().getTime()) / (60 * 1000);
                                                        minutoscitamedica2 = (int) (minutoscitamedica2 + (descuentorefrigerio <= 60 ? descuentorefrigerio : 60));
                                                    }
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutoscitamedica2) < 0 ? 0 : (!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutoscitamedica2);
                                                }
                                                if (minutoscitamedica3 > 0) {
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutoscitamedica3 < 0 ? dto.getMinutotardanza() - minutoscitamedica3 : 0)) < 0
                                                            ? 0
                                                            : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutoscitamedica3 < 0 ? dto.getMinutotardanza() - minutoscitamedica3 : 0)));
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutoscitamedica3) < 0 ? 0 : (dto.getMinutotardanza() - minutoscitamedica3));
                                                }

                                                if (minutosasuntosparticulares1 > 0) {
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutosasuntosparticulares1) < 0 ? 0 : (dto.getMinutotardanza() - minutosasuntosparticulares1));
                                                }
                                                if (minutosasuntosparticulares2 > 0) {
                                                    if (dto.getSalida() != null ? dto.getSalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() : dto.getSalida() != null) {
                                                        Long descuentorefrigerio = (EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() - dto.getSalida().getTime()) / (60 * 1000);
                                                        minutosasuntosparticulares2 = (int) (minutosasuntosparticulares2 + (descuentorefrigerio <= 60 ? descuentorefrigerio : 60));
                                                    }
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutosasuntosparticulares2) < 0 ? 0 : (!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutosasuntosparticulares2);
                                                }
                                                if (minutosasuntosparticulares3 > 0) {
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutosasuntosparticulares3 < 0 ? dto.getMinutotardanza() - minutosasuntosparticulares3 : 0)) < 0
                                                            ? 0
                                                            : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutosasuntosparticulares3 < 0 ? dto.getMinutotardanza() - minutosasuntosparticulares3 : 0)));
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutosasuntosparticulares3) < 0 ? 0 : (dto.getMinutotardanza() - minutosasuntosparticulares3));
                                                }

                                                if (minutoslimametropolitana1 > 0) {
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutoslimametropolitana1) < 0 ? 0 : (dto.getMinutotardanza() - minutoslimametropolitana1));
                                                }
                                                if (minutoslimametropolitana2 > 0) {
                                                    if (dto.getSalida() != null ? dto.getSalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() : dto.getSalida() != null) {
                                                        Long descuentorefrigerio = (EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() - dto.getSalida().getTime()) / (60 * 1000);
                                                        minutoslimametropolitana2 = (int) (minutoslimametropolitana2 + (descuentorefrigerio <= 60 ? descuentorefrigerio : 60));
                                                    }
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutoslimametropolitana2) < 0 ? 0 : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutoslimametropolitana2));
                                                }
                                                if (minutoslimametropolitana3 > 0) {
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutoslimametropolitana3 < 0 ? dto.getMinutotardanza() - minutoslimametropolitana3 : 0)) < 0
                                                            ? 0
                                                            : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutoslimametropolitana3 < 0 ? dto.getMinutotardanza() - minutoslimametropolitana3 : 0)));
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutoslimametropolitana3) < 0 ? 0 : (dto.getMinutotardanza() - minutoslimametropolitana3));
                                                }

                                                if (minutoscapmediajornada1 > 0) {
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutoscapmediajornada1) < 0 ? 0 : (dto.getMinutotardanza() - minutoscapmediajornada1));
                                                }
                                                if (minutoscapmediajornada2 > 0) {
                                                    if (dto.getSalida() != null ? dto.getSalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() : dto.getSalida() != null) {
                                                        Long descuentorefrigerio = (EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() - dto.getSalida().getTime()) / (60 * 1000);
                                                        minutoscapmediajornada2 = (int) (minutoscapmediajornada2 + (descuentorefrigerio <= 60 ? descuentorefrigerio : 60));
                                                    }
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutoscapmediajornada2) < 0 ? 0 : (!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutoscapmediajornada2);
                                                }
                                                if (minutoscapmediajornada3 > 0) {
                                                    dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutoscapmediajornada3 < 0 ? dto.getMinutotardanza() - minutoscapmediajornada3 : 0)) < 0
                                                            ? 0
                                                            : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutoscapmediajornada3 < 0 ? dto.getMinutotardanza() - minutoscapmediajornada3 : 0)));
                                                    dto.setMinutotardanza((dto.getMinutotardanza() - minutoscapmediajornada3) < 0 ? 0 : (dto.getMinutotardanza() - minutoscapmediajornada3));
                                                }

//                                            if (minutosremotoparcial1 > 0) {
//                                                dto.setMinutotardanza((dto.getMinutotardanza() - minutosremotoparcial1) < 0 ? 0 : (dto.getMinutotardanza() - minutosremotoparcial1));
//                                            }
//                                            if (minutosremotoparcial2 > 0) {
//                                                if (dto.getSalida() != null ? dto.getSalida().getTime() < EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() : dto.getSalida() != null) {
//                                                    Long descuentorefrigerio = (EAIUtil.formatoHoraOut.parse(propiedades.VALUE_HORA_GENERAL_REFRIGERIO_FIN).getTime() - dto.getSalida().getTime()) / (60 * 1000);
//                                                    minutosremotoparcial2 = (int) (minutosremotoparcial2 + (descuentorefrigerio <= 60 ? descuentorefrigerio : 60));
//                                                }
//                                                dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutosremotoparcial2) < 0 ? 0 : (!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) - minutosremotoparcial2);
//                                            }
//                                            if (minutosremotoparcial3 > 0) {
//                                                dto.setMinutofaltasalida(((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutosremotoparcial3 < 0 ? dto.getMinutotardanza() - minutosremotoparcial3 : 0)) < 0
//                                                        ? 0
//                                                        : ((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (dto.getMinutotardanza() - minutosremotoparcial3 < 0 ? dto.getMinutotardanza() - minutosremotoparcial3 : 0)));
//                                                dto.setMinutotardanza((dto.getMinutotardanza() - minutosremotoparcial3) < 0 ? 0 : (dto.getMinutotardanza() - minutosremotoparcial3));
//                                            }
                                                if (Optional.ofNullable(dto.getIngreso()).isPresent() && !Optional.ofNullable(dto.getSalida()).isPresent()) {
                                                    if ((temp.getHorariosalida().getTime() - dto.getIngreso().getTime()) < 0) {
//                                                        dto.setDiasustentada(false);
                                                        dto.setMinutotardanza(0);
                                                        dto.setMinutofaltasalida(0);
                                                        dto.setMinutoextrasalida(0);
                                                        dto.setHorasacompensar(0);
                                                    } else {
                                                        if (EAIUtil.formatoFechaOut.parse(listMarcacionPersonalFiltrado.get(0).getSfecha()).getDay() == 6 || EAIUtil.formatoFechaOut.parse(listMarcacionPersonalFiltrado.get(0).getSfecha()).getDay() == 0) {
                                                            dto.setMinutofaltasalida(0);
                                                        } else {
                                                            dto.setMinutofaltasalida((!Optional.ofNullable(dto.getMinutofaltasalida()).isPresent() ? 0 : dto.getMinutofaltasalida()) + (int) ((temp.getHorariosalida().getTime() - dto.getIngreso().getTime()) / (60 * 1000)));
                                                        }
//                                                        dto.setDiasustentada(true);
                                                    }
                                                    dto.setDiasustentada(false);
                                                } else if (((Long) (dto.getSalida().getTime() - dto.getIngreso().getTime()) / (60 * 1000)) <= 10) {
                                                    dto.setDiasustentada(false);
                                                } else {
                                                    if (minutoscompensacion1 > 0) {
                                                        Calendar calIngreso = Calendar.getInstance();
                                                        calIngreso.setTime(dto.getIngreso());
                                                        calIngreso.add(Calendar.MINUTE, -minutoscompensacion1);
                                                        dto.setIngreso(calIngreso.getTime());
                                                        dto.setSingreso(EAIUtil.formatoHoraOut.format(calIngreso.getTime()));
                                                    }
                                                    if (minutoscompensacion2 > 0) {
                                                        Calendar calSalida = Calendar.getInstance();
                                                        calSalida.setTime(dto.getSalida());
                                                        calSalida.add(Calendar.MINUTE, minutoscompensacion2);
                                                        dto.setSalida(calSalida.getTime());
                                                        dto.setSsalida(EAIUtil.formatoHoraOut.format(calSalida.getTime()));
                                                    }
                                                    if (minutoscompensacion1 > 0 || minutoscompensacion2 > 0) {
                                                        dto.setJornada((int) ((dto.getSalida().getTime() - dto.getIngreso().getTime()) / (60 * 60 * 1000)) - 1);
                                                    }
                                                    dto.setDiasustentada(true);
                                                }
                                            } else {
                                                dto.setMinutotardanza(0);
                                                dto.setMinutofaltasalida(0);
                                                dto.setMinutoextrasalida(0);
                                                dto.setHorasacompensar(0);
                                                dto.setDiasustentada(diaregularizado);
                                            }

                                            horastrabajadaspresencial = dto.getJornada();

                                            dto.setJornada((dto.getJornada() + horastrabajadas) > propiedades.VALUE_JORNADA_LABORAL_DIA_MAXIMO ? propiedades.VALUE_JORNADA_LABORAL_DIA_MAXIMO : (dto.getJornada() + horastrabajadas));
                                            dto.setDiasolicitada(diasolicitado);
                                            dto.setEsferiado(false);
                                            dto.setDetallecompleto(arrayDocumentosJSON.toString());

                                            JSONObject marcacionJSON = new JSONObject();
                                            marcacionJSON.put("idmarcacionpersonal", dto.getIdmarcacionpersonal());
                                            marcacionJSON.put("documentoidentidad", dto.getDocumentoidentidad());
                                            marcacionJSON.put("fecha", dto.getSfecha());
                                            marcacionJSON.put("sedeingreso", dto.getSedeingreso());
                                            marcacionJSON.put("sedesalida", dto.getSedesalida());
                                            marcacionJSON.put("ingreso", dto.getIngreso());
                                            marcacionJSON.put("salida", dto.getSalida());
                                            marcacionJSON.put("horasacompensar", dto.getHorasacompensar());
                                            marcacionJSON.put("horascompensadas", dto.getHorascompensadas());
                                            marcacionJSON.put("horastrabajopresencial", horastrabajadaspresencial);

                                            if (listControlAsistenciaParcialFiltrado.isEmpty()) {
                                                oReporteMarcacionDao.registrarControlAsistParcial(
                                                        new TControlAsistParcial(
                                                                propiedades.VALUE_TIPO_ESTADO_CNTRL_ASISTENCIA_PENDIENTE,
                                                                new Date(),
                                                                dto.getFecha(),
                                                                dto.getHorarioingreso(),
                                                                dto.getHorariosalida(),
                                                                dto.getIngreso(),
                                                                dto.getSalida(),
                                                                dto.getMinutotardanza(),
                                                                dto.getMinutoextrasalida(),
                                                                dto.getMinutofaltasalida(),
                                                                horastrabajadasremoto,
                                                                horastrabajadaspresencial,
                                                                dto.getJornada(),
                                                                dto.getHorasacompensar(),
                                                                horasatrabajar,
                                                                (horasatrabajar - dto.getJornada()) > 0 ? (horasatrabajar - dto.getJornada()) : 0,
                                                                minutospermisocongoce,
                                                                minutospermisosingoce,
                                                                horaslicenciacongoce,
                                                                horaslicenciasingoce,
                                                                horascomisiondeservicio,
                                                                minutossalud,
                                                                horasvacaciones,
                                                                horascapacitacion,
                                                                arrayDocumentosJSON.length() != 0 ? arrayDocumentosJSON.toString() : null,
                                                                marcacionJSON.length() != 0 ? marcacionJSON.toString() : null,
                                                                dto.isDiasustentada(),
                                                                dto.isDiasolicitada(),
                                                                dto.isEsferiado(),
                                                                new Date(),
                                                                Constantes.CODIGO_ESTADO_ACTIVO,
                                                                new TPersonal(temp.getIdpersonal()),
                                                                temp.getDocumentoidentidad()
                                                        ));
                                            } else {

                                                Map<String, Object> condicional = new HashMap<>();
                                                condicional.put("idcontrolasistparcial", listControlAsistenciaParcialFiltrado.get(0).getIdcontrolasistparcial());

                                                Map<String, Object> parametros = new HashMap<>();
                                                parametros.put("idcontrolasistparcial", listControlAsistenciaParcialFiltrado.get(0).getIdcontrolasistparcial());
                                                parametros.put("fechaproceso", new Date());
                                                parametros.put("horarioingreso", dto.getHorarioingreso());
                                                parametros.put("horariosalida", dto.getHorariosalida());
                                                parametros.put("ingreso", dto.getIngreso());
                                                parametros.put("salida", dto.getSalida());
                                                parametros.put("minutotardanza", dto.getMinutotardanza());
                                                parametros.put("minutoextrasalida", dto.getMinutoextrasalida());
                                                parametros.put("minutofaltasalida", dto.getMinutofaltasalida());
                                                parametros.put("horastrabajadasremoto", horastrabajadasremoto);
                                                parametros.put("horastrabajadaspresencial", horastrabajadaspresencial);
                                                parametros.put("horastrabajadas", dto.getJornada());
                                                parametros.put("horasacompensar", dto.getHorasacompensar());
                                                parametros.put("horasatrabajar", horasatrabajar);
                                                parametros.put("horasrestante", (horasatrabajar - dto.getJornada()) > 0 ? (horasatrabajar - dto.getJornada()) : 0);
                                                parametros.put("minutospermisocongoce", minutospermisocongoce);
                                                parametros.put("minutospermisosingoce", minutospermisosingoce);
                                                parametros.put("horaslicenciacongoce", horaslicenciacongoce);
                                                parametros.put("horaslicenciasingoce", horaslicenciasingoce);
                                                parametros.put("horascomisiondeservicio", horascomisiondeservicio);
                                                parametros.put("minutossalud", minutossalud);
                                                parametros.put("horasvacaciones", horasvacaciones);
                                                parametros.put("horascapacitacion", horascapacitacion);
                                                parametros.put("documentosjson", arrayDocumentosJSON.length() != 0 ? arrayDocumentosJSON.toString() : null);
                                                parametros.put("marcacionjson", marcacionJSON.length() != 0 ? marcacionJSON.toString() : null);
                                                parametros.put("diasustentada", dto.isDiasustentada());
                                                parametros.put("diasolicitada", dto.isDiasolicitada());
                                                parametros.put("esferiado", dto.isEsferiado());
                                                parametros.put("fechamodificacion", new Date());

                                                oReporteMarcacionDao.updateQuery(TControlAsistParcial.class.getSimpleName(), parametros, condicional);
                                            }

                                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ParseException | DBException | JSONException ex) {
                                            java.util.logging.Logger.getLogger(ControlAsistenciaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                            logger.error("Error: {}", ex);
                                        }
                                    } else {

                                        try {

                                            if (lddesde.plusDays(i).getDayOfWeek().getValue() != 6 && lddesde.plusDays(i).getDayOfWeek().getValue() != 7 || diaregularizado) {

                                                List<TFeriado> listFeriadoFiltrado = listFeriado.stream()
                                                        .filter((TFeriado tfer)
                                                                -> isWithinRange(Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()), tfer.getFechadesde(), tfer.getFechahasta())
                                                        ).collect(Collectors.toList());

                                                if (listFeriadoFiltrado.isEmpty()) {

                                                    Integer subtotal1
                                                            = minutoscitacion1
                                                            + minutoscompensacion1
                                                            + minutoscapacitacionoficializada1
                                                            + minutospermisocongoceotros1
                                                            + minutoscitamedica1
                                                            + minutoslimametropolitana1
                                                            + minutoscapmediajornada1
                                                            + minutosasuntosparticulares1;

                                                    Integer subtotal2
                                                            = minutoscitacion2
                                                            + minutoscompensacion2
                                                            + minutoscapacitacionoficializada2
                                                            + minutospermisocongoceotros2
                                                            + minutoscitamedica2
                                                            + minutoslimametropolitana2
                                                            + minutoscapmediajornada2
                                                            + minutosasuntosparticulares2;

                                                    Integer subtotal3
                                                            = minutoscitacion3
                                                            + minutoscompensacion3
                                                            + minutoscapacitacionoficializada3
                                                            + minutospermisocongoceotros3
                                                            + minutoscitamedica3
                                                            + minutoslimametropolitana3
                                                            + minutoscapmediajornada3
                                                            + minutosasuntosparticulares3;

                                                    Integer tardanza1 = 0;
                                                    Integer tardanza2 = 0;
                                                    Integer minutosfaltasalida1 = 0;
                                                    Integer minutosfaltasalida2 = 0;
                                                    Integer adicional1 = 0;
                                                    Integer adicional2 = 0;

//                                                    if (!diaregularizado) {
//
//                                                        if (subtotal1 > 0 && subtotal2 > 0) {
//
//                                                            if (subtotal1 + subtotal2 >= 480) {
//
//                                                                tardanza1 = 0;
//                                                                minutosfaltasalida1 = 0;
//
//                                                            } else {
//
//                                                                if (subtotal1 <= 270) {
//                                                                    tardanza1 = tardanza1 + (270 - subtotal1);
//                                                                } else {
//                                                                    tardanza1 = 0;
//                                                                    adicional1 = subtotal1 - 270;
//                                                                }
//
//                                                                if (subtotal2 <= 210) {
//                                                                    minutosfaltasalida1 = minutosfaltasalida1 + (210 - subtotal2);
//                                                                } else {
//                                                                    minutosfaltasalida1 = 0;
//                                                                    adicional2 = subtotal2 - 210;
//                                                                }
//                                                            }
//                                                        } else {
//
//                                                            if (subtotal1 > 0) {
//                                                                if (subtotal1 <= 270) {
//                                                                    tardanza1 = tardanza1 + (270 - subtotal1);
//                                                                } else {
//                                                                    if (subtotal1 < 330) {
//                                                                        minutosfaltasalida2 = 210;
//                                                                    } else {
//                                                                        minutosfaltasalida2 = 540 - subtotal1;
//                                                                    }
//                                                                    tardanza1 = 0;
//                                                                    adicional1 = subtotal1 - 270;
//                                                                }
//                                                            }
//
//                                                            if (subtotal2 > 0) {
//                                                                if (subtotal2 <= 210) {
//                                                                    minutosfaltasalida1 = minutosfaltasalida1 + (210 - subtotal2);
//                                                                } else {
//                                                                    if (subtotal2 <= 270) {
//                                                                        tardanza2 = 270;
//                                                                    } else {
//                                                                        tardanza2 = 540 - subtotal2;
//                                                                    }
//                                                                    minutosfaltasalida1 = 0;
//                                                                    adicional2 = subtotal2 - 210;
//                                                                }
//                                                            }
//                                                        }
//                                                        if (tardanza1 + minutosfaltasalida1 > 0) {
//                                                            diaregularizado = true;
//                                                            if (adicional1 > 0) {
//                                                                minutosfaltasalida1 = minutosfaltasalida1 - adicional1;
//                                                            }
//                                                            if (adicional2 > 0) {
//                                                                tardanza1 = tardanza1 - adicional1;
//                                                            }
//                                                        } else {
//                                                            if (minutosfaltasalida2 > 0) {
//                                                                diaregularizado = true;
//                                                                minutosfaltasalida1 = minutosfaltasalida1 + minutosfaltasalida2;
//                                                            }
//                                                            if (tardanza2 > 0) {
//                                                                diaregularizado = true;
//                                                                tardanza1 = tardanza1 + tardanza2;
//                                                            }
//                                                        }
//
//                                                        if (subtotal3 > 0) {
//                                                            minutosfaltasalida1 = minutosfaltasalida1 + (tardanza1 - subtotal3 < 0 ? tardanza1 - subtotal3 : 0);
//                                                            tardanza1 = tardanza1 - subtotal3 < 0 ? 0 : tardanza1 - subtotal3;
//                                                        }
//                                                    }
                                                    if (!(Optional.ofNullable(temp.getIddependenciadesignado()).isPresent() || temp.getExoneradomarcar() != 1) || diaregularizado) {

                                                        if (listControlAsistenciaParcialFiltrado.isEmpty()) {

                                                            oReporteMarcacionDao.registrarControlAsistParcial(
                                                                    new TControlAsistParcial(
                                                                            propiedades.VALUE_TIPO_ESTADO_CNTRL_ASISTENCIA_PENDIENTE,
                                                                            new Date(),
                                                                            Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                                                            horastrabajadasremoto,
                                                                            horastrabajadaspresencial,
                                                                            horastrabajadas,
                                                                            0,
                                                                            horasatrabajar,
                                                                            (horasatrabajar - horastrabajadas) > 0 ? (horasatrabajar - horastrabajadas) : 0,
                                                                            minutospermisocongoce,
                                                                            minutospermisosingoce,
                                                                            horaslicenciacongoce,
                                                                            horaslicenciasingoce,
                                                                            horascomisiondeservicio,
                                                                            minutossalud,
                                                                            horasvacaciones,
                                                                            horascapacitacion,
                                                                            arrayDocumentosJSON.length() != 0 ? arrayDocumentosJSON.toString() : null,
                                                                            diaregularizado,
                                                                            diasolicitado,
                                                                            false,
                                                                            new Date(),
                                                                            Constantes.CODIGO_ESTADO_ACTIVO,
                                                                            new TPersonal(temp.getIdpersonal()),
                                                                            temp.getDocumentoidentidad()
                                                                    ));

                                                        } else {

                                                            Map<String, Object> condicional = new HashMap<>();
                                                            condicional.put("idcontrolasistparcial", listControlAsistenciaParcialFiltrado.get(0).getIdcontrolasistparcial());

                                                            Map<String, Object> parametros = new HashMap<>();
                                                            parametros.put("idcontrolasistparcial", listControlAsistenciaParcialFiltrado.get(0).getIdcontrolasistparcial());
                                                            parametros.put("fechaproceso", new Date());
//                                            parametros.put("horarioingreso", dto.getHorarioingreso());
//                                            parametros.put("horariosalida", dto.getHorariosalida());
//                                            parametros.put("ingreso", dto.getIngreso());
//                                            parametros.put("salida", dto.getSalida());
//                                            parametros.put("minutotardanza", dto.getMinutotardanza());
//                                            parametros.put("minutoextrasalida", dto.getMinutoextrasalida());
//                                            parametros.put("minutofaltasalida", dto.getMinutofaltasalida());
                                                            parametros.put("horastrabajadasremoto", horastrabajadasremoto);
                                                            parametros.put("horastrabajadaspresencial", horastrabajadaspresencial);
                                                            parametros.put("horastrabajadas", horastrabajadas);
                                                            parametros.put("horasacompensar", 0);
                                                            parametros.put("horasatrabajar", horasatrabajar);
                                                            parametros.put("horasrestante", (horasatrabajar - horastrabajadas) > 0 ? (horasatrabajar - horastrabajadas) : 0);
                                                            parametros.put("minutospermisocongoce", minutospermisocongoce);
                                                            parametros.put("minutospermisosingoce", minutospermisosingoce);
                                                            parametros.put("horaslicenciacongoce", horaslicenciacongoce);
                                                            parametros.put("horaslicenciasingoce", horaslicenciasingoce);
                                                            parametros.put("horascomisiondeservicio", horascomisiondeservicio);
                                                            parametros.put("minutossalud", minutossalud);
                                                            parametros.put("horasvacaciones", horasvacaciones);
                                                            parametros.put("horascapacitacion", horascapacitacion);
                                                            parametros.put("documentosjson", arrayDocumentosJSON.length() != 0 ? arrayDocumentosJSON.toString() : null);
                                                            parametros.put("diasustentada", diaregularizado);
                                                            parametros.put("diasolicitada", diasolicitado);
                                                            parametros.put("esferiado", false);
                                                            parametros.put("fechamodificacion", new Date());

                                                            oReporteMarcacionDao.updateQuery(TControlAsistParcial.class.getSimpleName(), parametros, condicional);

                                                        }
                                                    } else if (Optional.ofNullable(listControlAsistenciaParcialFiltrado).isPresent()
                                                            && !listControlAsistenciaParcialFiltrado.isEmpty()
                                                            && Optional.ofNullable(listControlAsistenciaParcialFiltrado.get(0).getIdcontrolasistparcial()).isPresent()) {
                                                        oReporteMarcacionDao.eliminarControlAsistParcial(listControlAsistenciaParcialFiltrado.get(0));
                                                    }

                                                } else if (diaregularizado) {

                                                    if (listControlAsistenciaParcialFiltrado.isEmpty()) {

                                                        oReporteMarcacionDao.registrarControlAsistParcial(
                                                                new TControlAsistParcial(
                                                                        propiedades.VALUE_TIPO_ESTADO_CNTRL_ASISTENCIA_PENDIENTE,
                                                                        new Date(),
                                                                        Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                                                        horastrabajadasremoto,
                                                                        horastrabajadaspresencial,
                                                                        horastrabajadas,
                                                                        0,
                                                                        horasatrabajar,
                                                                        (horasatrabajar - horastrabajadas) > 0 ? (horasatrabajar - horastrabajadas) : 0,
                                                                        minutospermisocongoce,
                                                                        minutospermisosingoce,
                                                                        horaslicenciacongoce,
                                                                        horaslicenciasingoce,
                                                                        horascomisiondeservicio,
                                                                        minutossalud,
                                                                        horasvacaciones,
                                                                        horascapacitacion,
                                                                        arrayDocumentosJSON.length() != 0 ? arrayDocumentosJSON.toString() : null,
                                                                        diaregularizado,
                                                                        diasolicitado,
                                                                        true,
                                                                        new Date(),
                                                                        Constantes.CODIGO_ESTADO_ACTIVO,
                                                                        new TPersonal(temp.getIdpersonal()),
                                                                        temp.getDocumentoidentidad()
                                                                ));

                                                    } else {

                                                        Map<String, Object> condicional = new HashMap<>();
                                                        condicional.put("idcontrolasistparcial", listControlAsistenciaParcialFiltrado.get(0).getIdcontrolasistparcial());

                                                        Map<String, Object> parametros = new HashMap<>();
                                                        parametros.put("idcontrolasistparcial", listControlAsistenciaParcialFiltrado.get(0).getIdcontrolasistparcial());
                                                        parametros.put("fechaproceso", new Date());
//                                            parametros.put("horarioingreso", dto.getHorarioingreso());
//                                            parametros.put("horariosalida", dto.getHorariosalida());
//                                            parametros.put("ingreso", dto.getIngreso());
//                                            parametros.put("salida", dto.getSalida());
//                                            parametros.put("minutotardanza", dto.getMinutotardanza());
//                                            parametros.put("minutoextrasalida", dto.getMinutoextrasalida());
//                                            parametros.put("minutofaltasalida", dto.getMinutofaltasalida());
                                                        parametros.put("horastrabajadasremoto", horastrabajadasremoto);
                                                        parametros.put("horastrabajadaspresencial", horastrabajadaspresencial);
                                                        parametros.put("horastrabajadas", horastrabajadas);
                                                        parametros.put("horasacompensar", 0);
                                                        parametros.put("horasatrabajar", horasatrabajar);
                                                        parametros.put("horasrestante", (horasatrabajar - horastrabajadas) > 0 ? (horasatrabajar - horastrabajadas) : 0);
                                                        parametros.put("minutospermisocongoce", minutospermisocongoce);
                                                        parametros.put("minutospermisosingoce", minutospermisosingoce);
                                                        parametros.put("horaslicenciacongoce", horaslicenciacongoce);
                                                        parametros.put("horaslicenciasingoce", horaslicenciasingoce);
                                                        parametros.put("horascomisiondeservicio", horascomisiondeservicio);
                                                        parametros.put("minutossalud", minutossalud);
                                                        parametros.put("horasvacaciones", horasvacaciones);
                                                        parametros.put("horascapacitacion", horascapacitacion);
                                                        parametros.put("documentosjson", arrayDocumentosJSON.length() != 0 ? arrayDocumentosJSON.toString() : null);
                                                        parametros.put("diasustentada", diaregularizado);
                                                        parametros.put("diasolicitada", diasolicitado);
                                                        parametros.put("esferiado", true);
                                                        parametros.put("fechamodificacion", new Date());

                                                        oReporteMarcacionDao.updateQuery(TControlAsistParcial.class.getSimpleName(), parametros, condicional);

                                                    }

                                                } else if (!(Optional.ofNullable(temp.getIddependenciadesignado()).isPresent() || temp.getExoneradomarcar() != 1)) {

                                                    JSONObject documentoJSON = new JSONObject();
                                                    documentoJSON.put("idferiado", listFeriadoFiltrado.get(0).getIdferiado());
                                                    documentoJSON.put("descripcion", listFeriadoFiltrado.get(0).getDescripcion());
                                                    documentoJSON.put("documentoreferencia", listFeriadoFiltrado.get(0).getDocumentoreferencia());
                                                    documentoJSON.put("escompensable", listFeriadoFiltrado.get(0).getEscompensable());
                                                    documentoJSON.put("fechadesde", Optional.ofNullable(listFeriadoFiltrado.get(0).getFechadesde()).isPresent() ? EAIUtil.formatoFechaOut.format(listFeriadoFiltrado.get(0).getFechadesde()) : null);
                                                    documentoJSON.put("fechahasta", Optional.ofNullable(listFeriadoFiltrado.get(0).getFechahasta()).isPresent() ? EAIUtil.formatoFechaOut.format(listFeriadoFiltrado.get(0).getFechahasta()) : null);
                                                    documentoJSON.put("fechainiciorecuper", Optional.ofNullable(listFeriadoFiltrado.get(0).getFechainiciorecuper()).isPresent() ? EAIUtil.formatoFechaOut.format(listFeriadoFiltrado.get(0).getFechainiciorecuper()) : null);
                                                    documentoJSON.put("fechafinrecuper", Optional.ofNullable(listFeriadoFiltrado.get(0).getFechafinrecuper()).isPresent() ? EAIUtil.formatoFechaOut.format(listFeriadoFiltrado.get(0).getFechafinrecuper()) : null);

                                                    if (arrayDocumentosJSON.length() == 0) {
                                                        arrayDocumentosJSON.put(documentoJSON);
                                                    }

                                                    if (listControlAsistenciaParcialFiltrado.isEmpty()) {

                                                        oReporteMarcacionDao.registrarControlAsistParcial(
                                                                new TControlAsistParcial(
                                                                        propiedades.VALUE_TIPO_ESTADO_CNTRL_ASISTENCIA_PENDIENTE,
                                                                        new Date(),
                                                                        Date.from(lddesde.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                                                        horastrabajadasremoto,
                                                                        horastrabajadaspresencial,
                                                                        horastrabajadas,
                                                                        0,
                                                                        horastrabajadas,
                                                                        0,
                                                                        //                                                                        propiedades.VALUE_JORNADA_LABORAL_DIA,
                                                                        //                                                                        (propiedades.VALUE_JORNADA_LABORAL_DIA - horastrabajadas) > 0 ? (propiedades.VALUE_JORNADA_LABORAL_DIA - horastrabajadas) : 0,
                                                                        minutospermisocongoce,
                                                                        minutospermisosingoce,
                                                                        horaslicenciacongoce,
                                                                        horaslicenciasingoce,
                                                                        horascomisiondeservicio,
                                                                        minutossalud,
                                                                        horasvacaciones,
                                                                        horascapacitacion,
                                                                        arrayDocumentosJSON.length() != 0 ? arrayDocumentosJSON.toString() : null,
                                                                        Optional.ofNullable(listFeriadoFiltrado.get(0).getEscompensable()).isPresent() ? !listFeriadoFiltrado.get(0).getEscompensable() : false,
                                                                        diasolicitado,
                                                                        true,
                                                                        new Date(),
                                                                        Constantes.CODIGO_ESTADO_ACTIVO,
                                                                        new TPersonal(temp.getIdpersonal()),
                                                                        temp.getDocumentoidentidad()
                                                                ));

                                                    } else {

                                                        Map<String, Object> condicional = new HashMap<>();
                                                        condicional.put("idcontrolasistparcial", listControlAsistenciaParcialFiltrado.get(0).getIdcontrolasistparcial());

                                                        Map<String, Object> parametros = new HashMap<>();
                                                        parametros.put("idcontrolasistparcial", listControlAsistenciaParcialFiltrado.get(0).getIdcontrolasistparcial());
                                                        parametros.put("fechaproceso", new Date());
//                                            parametros.put("horarioingreso", dto.getHorarioingreso());
//                                            parametros.put("horariosalida", dto.getHorariosalida());
//                                            parametros.put("ingreso", dto.getIngreso());
//                                            parametros.put("salida", dto.getSalida());
//                                            parametros.put("minutotardanza", dto.getMinutotardanza());
//                                            parametros.put("minutoextrasalida", dto.getMinutoextrasalida());
//                                            parametros.put("minutofaltasalida", dto.getMinutofaltasalida());
                                                        parametros.put("horastrabajadasremoto", horastrabajadasremoto);
                                                        parametros.put("horastrabajadaspresencial", horastrabajadaspresencial);
                                                        parametros.put("horastrabajadas", horastrabajadas);
                                                        parametros.put("horasacompensar", 0);
                                                        parametros.put("horasatrabajar", horastrabajadas);
                                                        parametros.put("horasrestante", 0);
//                                                        parametros.put("horasatrabajar", propiedades.VALUE_JORNADA_LABORAL_DIA);
//                                                        parametros.put("horasrestante", (propiedades.VALUE_JORNADA_LABORAL_DIA - horastrabajadas) > 0 ? (propiedades.VALUE_JORNADA_LABORAL_DIA - horastrabajadas) : 0);
                                                        parametros.put("minutospermisocongoce", minutospermisocongoce);
                                                        parametros.put("minutospermisosingoce", minutospermisosingoce);
                                                        parametros.put("horaslicenciacongoce", horaslicenciacongoce);
                                                        parametros.put("horaslicenciasingoce", horaslicenciasingoce);
                                                        parametros.put("horascomisiondeservicio", horascomisiondeservicio);
                                                        parametros.put("minutossalud", minutossalud);
                                                        parametros.put("horasvacaciones", horasvacaciones);
                                                        parametros.put("horascapacitacion", horascapacitacion);
                                                        parametros.put("documentosjson", arrayDocumentosJSON.length() != 0 ? arrayDocumentosJSON.toString() : null);
                                                        parametros.put("diasustentada", Optional.ofNullable(listFeriadoFiltrado.get(0).getEscompensable()).isPresent() ? !listFeriadoFiltrado.get(0).getEscompensable() : false);
                                                        parametros.put("diasolicitada", diasolicitado);
                                                        parametros.put("esferiado", true);
                                                        parametros.put("fechamodificacion", new Date());

                                                        oReporteMarcacionDao.updateQuery(TControlAsistParcial.class.getSimpleName(), parametros, condicional);
                                                    }
                                                }
                                            }

                                        } catch (DBException | JSONException ex) {
                                            java.util.logging.Logger.getLogger(ControlAsistenciaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                            } catch (ParseException | JSONException ex) {
                                java.util.logging.Logger.getLogger(ControlAsistenciaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                        return null;

                    }).collect(Collectors.toList());

            response.setCodigo(Constantes.CODIGO_RPSTA_OK);
            response.setMensaje(Constantes.RPSTA_OK);

            if (Optional.ofNullable(AuditUtil.getCorreoinstitucional()).isPresent() ? idpersonal.length() == 0 && documentoidentidad.length() == 0 : false) {
                Map<String, Object> requestJSON = new HashMap<>();
                requestJSON.put("to", Arrays.asList(AuditUtil.getCorreoinstitucional()));
                requestJSON.put("subject", "ESTADO DEL PROCESO DE MARCACIONES");
                requestJSON.put("body", "SATISFACTORIO: TERMINO EL PROCESO DE MARCACIONES SELECCIONADAS, FAVOR DE REFRESCAR SU NAVEGADOR. \n\n\n GRACIAS");
                oUtilitariosWS.sendMail(requestJSON);
            }

        } catch (ParseException | DBException ex) {
            java.util.logging.Logger.getLogger(ControlAsistenciaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            logger.error("Error: {}", ex);

            if (Optional.ofNullable(AuditUtil.getCorreoinstitucional()).isPresent() ? idpersonal.length() == 0 && documentoidentidad.length() == 0 : false) {
                Map<String, Object> requestJSON = new HashMap<>();
                requestJSON.put("to", Arrays.asList(AuditUtil.getCorreoinstitucional()));
                requestJSON.put("subject", "ESTADO DEL PROCESO DE MARCACIONES");
                requestJSON.put("body", "ERROR: NO TERMINO EL PROCESO DE MARCACIONES SELECCIONADAS, FAVOR DE COMUNICAR A MAP. \n\n\n GRACIAS");
                oUtilitariosWS.sendMail(requestJSON);
            }
            */

        } catch(ParseException | DBException ex){
            java.util.logging.Logger.getLogger(ControlAsistenciaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            logger.error("Error: {}", ex);
        }


        response.setCodigo(Constantes.CODIGO_RPSTA_OK);
        response.setMensaje(Constantes.RPSTA_OK);

        logger.info("::::::::::::::::::: FIN registrarcontrolasistenciaparcial :::::::::::::::::");

        return response;
    }


}
