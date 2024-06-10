package spring2ws.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class Propiedades {

/*
    @Value("${service.host}")
    public String SERVICE_HOST;

    @Value("${service.personalws}")
    public String SERVICE_PERSONAL_WS;

    @Value("${service.utilitariosws}")
    public String SERVICE_UTILITARIOS_WS;
*/
    //HORARIO DE TRABAJO
    @Value("${value.hora.general.ingreso}")
    public String VALUE_HORA_GENERAL_INGRESO;

    @Value("${value.hora.general.salida}")
    public String VALUE_HORA_GENERAL_SALIDA;

    @Value("${value.hora.general.lactancia.inicio}")
    public String VALUE_HORA_GENERAL_LACTANCIA_INICIO;

    @Value("${value.hora.general.lactancia.fin}")
    public String VALUE_HORA_GENERAL_LACTANCIA_FIN;

    @Value("${value.hora.general.refrigerio.inicio}")
    public String VALUE_HORA_GENERAL_REFRIGERIO_INICIO;

    @Value("${value.hora.general.refrigerio.fin}")
    public String VALUE_HORA_GENERAL_REFRIGERIO_FIN;

    @Value("${value.joranada.laboral.dia}")
    public int VALUE_JORNADA_LABORAL_DIA;

    @Value("${value.joranada.laboral.dia.maximo}")
    public int VALUE_JORNADA_LABORAL_DIA_MAXIMO;

    //TIPOS DE SOLICITUDES
    @Value("${value.tipo.sol.persmiso.con.goce}")
    public int VALUE_TIPO_SOL_PERMISO_CON_GOCE;

    @Value("${value.tipo.sol.persmiso.con.goce.onomastico}")
    public int VALUE_TIPO_SOL_PERMISO_CON_GOCE_ONOMASTICO;

    @Value("${value.tipo.sol.persmiso.con.goce.citacionjudicial}")
    public int VALUE_TIPO_SOL_PERMISO_CON_GOCE_CITACIONJUDICIAL;

    @Value("${value.tipo.sol.persmiso.con.goce.capacitacionofic}")
    public int VALUE_TIPO_SOL_PERMISO_CON_GOCE_CAPACITACIONOFIC;

    @Value("${value.tipo.sol.persmiso.con.goce.lactancia}")
    public int VALUE_TIPO_SOL_PERMISO_CON_GOCE_LACTANCIA;

    @Value("${value.tipo.sol.persmiso.con.goce.otros}")
    public int VALUE_TIPO_SOL_PERMISO_CON_GOCE_OTROS;

    @Value("${value.tipo.sol.persmiso.con.goce.capacitacion}")
    public int VALUE_TIPO_SOL_PERMISO_CON_GOCE_CAPACITACION;

    @Value("${value.tipo.sol.persmiso.sin.goce}")
    public int VALUE_TIPO_SOL_PERMISO_SIN_GOCE;

    @Value("${value.tipo.sol.persmiso.sin.goce.asuntosparticulares}")
    public int VALUE_TIPO_SOL_PERMISO_SIN_GOCE_ASUNTOSPARTICULARES;

    @Value("${value.tipo.sol.persmiso.salud}")
    public int VALUE_TIPO_SOL_PERMISO_SALUD;

    @Value("${value.tipo.sol.persmiso.salud.descansomedico}")
    public int VALUE_TIPO_SOL_PERMISO_SALUD_DESCANSOMEDICO;

    @Value("${value.tipo.sol.persmiso.salud.citamedica}")
    public int VALUE_TIPO_SOL_PERMISO_SALUD_CITAMEDICA;

    @Value("${value.tipo.sol.persmiso.salud.subsidio}")
    public int VALUE_TIPO_SOL_PERMISO_SALUD_SUBSIDIO;

    @Value("${value.tipo.sol.persmiso.compensable}")
    public int VALUE_TIPO_SOL_PERMISO_COMPENSABLE;

    @Value("${value.tipo.sol.persmiso.compensable.elecciones}")
    public int VALUE_TIPO_SOL_PERMISO_COMPENSABLE_ELECCIONES;

    @Value("${value.tipo.sol.persmiso.compensable.feriadoscomp}")
    public int VALUE_TIPO_SOL_PERMISO_COMPENSABLE_FERIADOCOMP;

    @Value("${value.tipo.sol.persmiso.compensable.feriadosreg}")
    public int VALUE_TIPO_SOL_PERMISO_COMPENSABLE_FERIADOREG;

    @Value("${value.tipo.sol.persmiso.compensable.docencia}")
    public int VALUE_TIPO_SOL_PERMISO_COMPENSABLE_DOCENCIA;

    @Value("${value.tipo.sol.comision.servicio}")
    public int VALUE_TIPO_SOL_COMISION_SERVICIO;

    @Value("${value.tipo.sol.comision.servicio.limametropolitana}")
    public int VALUE_TIPO_SOL_COMISION_SERVICIO_LIMAMETROPOLITANA;

    @Value("${value.tipo.sol.comision.servicio.viajeinteriorpais}")
    public int VALUE_TIPO_SOL_COMISION_SERVICIO_VIAJEINTERIORPAIS;

    @Value("${value.tipo.sol.comision.servicio.viajeexteriorpais}")
    public int VALUE_TIPO_SOL_COMISION_SERVICIO_VIAJEEXTERIORPAIS;

    @Value("${value.tipo.sol.trabajo.remoto}")
    public int VALUE_TIPO_SOL_TRABAJO_REMOTO;

    @Value("${value.tipo.sol.trabajo.remoto.trabajoremoto}")
    public int VALUE_TIPO_SOL_TRABAJO_REMOTO_TRABAJOREMOTO;

    @Value("${value.tipo.sol.licencia.con.goce}")
    public int VALUE_TIPO_SOL_LICENCIA_CON_GOCE;

    @Value("${value.tipo.sol.licencia.con.goce.fallecimientofam}")
    public int VALUE_TIPO_SOL_LICENCIA_CON_GOCE_FALLECIMIENTOFAM;

    @Value("${value.tipo.sol.licencia.con.goce.maternidad}")
    public int VALUE_TIPO_SOL_LICENCIA_CON_GOCE_MATERNIDAD;

    @Value("${value.tipo.sol.licencia.con.goce.paternidad}")
    public int VALUE_TIPO_SOL_LICENCIA_CON_GOCE_PATERNIDAD;

    @Value("${value.tipo.sol.licencia.con.goce.enfermedadfam}")
    public int VALUE_TIPO_SOL_LICENCIA_CON_GOCE_ENFERMEDADFAM;

    @Value("${value.tipo.sol.licencia.con.goce.rehabilitacion}")
    public int VALUE_TIPO_SOL_LICENCIA_CON_GOCE_REHABILITACION;

    @Value("${value.tipo.sol.licencia.con.goce.emergenciacovid}")
    public int VALUE_TIPO_SOL_LICENCIA_CON_GOCE_EMERGENCIACOVID;

    @Value("${value.tipo.sol.licencia.sin.goce}")
    public int VALUE_TIPO_SOL_LICENCIA_SIN_GOCE;

    @Value("${value.tipo.sol.licencia.sin.goce.remuneraciones}")
    public int VALUE_TIPO_SOL_LICENCIA_SIN_GOCE_REMUNERACIONES;

    @Value("${value.tipo.sol.compensacion}")
    public int VALUE_TIPO_SOL_COMPENSCION;

    @Value("${value.tipo.sol.compensacion.solicitudcompensacion}")
    public int VALUE_TIPO_SOL_COMPENSCION_SOLICITUDCOMPENSACION;

    @Value("${value.tipo.sol.vacaciones}")
    public int VALUE_TIPO_SOL_VACACIONES;

    @Value("${value.tipo.sol.vacaciones.fraccionado}")
    public int VALUE_TIPO_SOL_VACACIONES_FRACCIONADO;

    @Value("${value.tipo.sol.vacaciones.adelanto}")
    public int VALUE_TIPO_SOL_VACACIONES_ADELANTO;

    @Value("${value.tipo.sol.vacaciones.normal}")
    public int VALUE_TIPO_SOL_VACACIONES_NORMAL;

    @Value("${value.tipo.sol.vacaciones.mediajornada}")
    public int VALUE_TIPO_SOL_VACACIONES_MEDIAJORNADA;

    @Value("${value.tipo.sol.medidacautelar}")
    public int VALUE_TIPO_SOL_MEDIDACAUTELAR;

    @Value("${value.tipo.sol.medidacautelar.medidacautelar}")
    public int VALUE_TIPO_SOL_MEDIDACAUTELAR_MEDIDACAUTELAR;

    @Value("${value.tipo.sol.teletrabajo}")
    public int VALUE_TIPO_SOL_TELETRABAJO;

    @Value("${value.tipo.sol.teletrabajo.teletrabajo}")
    public int VALUE_TIPO_SOL_TELETRABAJO_TELETRABAJO;

    //ESTADOS O VALORES FIJOS
    @Value("${value.tipo.estado.sol.aprobado}")
    public int VALUE_TIPO_ESTADO_SOL_APROBADO;

    @Value("${value.tipo.estado.vac.gozado}")
    public int VALUE_TIPO_ESTADO_VAC_GOZADO;

    @Value("${value.tipo.estado.cntrl.asistencia.pendiente}")
    public int VALUE_TIPO_ESTADO_CNTRL_ASISTENCIA_PENDIENTE;
}
