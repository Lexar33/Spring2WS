package spring2ws.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class VwPersonalDto {


    private int idpersona;
    private String nombres;
    private String apellidopaterno;
    private String apellidomaterno;
    private String documentoidentidad;
    private String fechanacimiento;
    private Integer idpersonal;
    private Integer idtestado;
    private String urlimagen;
    private Integer idsede;
    private String descripcionsede;
    private Integer idcontrato;
    private String fechainicio;
    private Date dfechainiciocontrato;
    private String fechafin;
    private Date dfechafincontrato;
    private String fechacese;
    private Date dfechacesecontrato;
    private String nombrecargo;
    private Double remuneracion;
    private Integer iddependencia;
    private String unidad;
    private String areaequipo;
    private String genero;
    private String tipodoc;
    private String estadopersonal;
    private String tipocontrato;
    private String url;
    private String fechainicioadenda;
    private String fechafinadenda;
    private Date dfechafinadenda;
    private String fechabajaadenda;
    private Date dfechabajaadenda;
    private Integer exoneradomarcar;
    private String correoinstitucional;
    private Integer idhorariotrabajo;
    private Date horarioingreso;
    private Date horariosalida;
    private String iddependenciaencargado;
    private String iddependenciadelegado;
    private String iddependenciadesignado;
    private String iddependenciaencargadosuperior;
    private String iddependenciadelegadosuperior;
    private String iddependenciadesignadosuperior;

}
