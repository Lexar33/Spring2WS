package spring2ws.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "T_CONTROL_ASIST_MENSUAL")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TControlAsistMensual.findAll", query = "SELECT t FROM TControlAsistMensual t")
        ,
        @NamedQuery(name = "TControlAsistMensual.findByPersonalDate", query = "SELECT t FROM TControlAsistMensual t WHERE "
                + "( :idpersonal IS NULL OR (:idpersonal IS NOT NULL AND t.idpersonal.idpersonal = :idpersonal)) AND "
                + "( :mes IS NULL OR (:mes IS NOT NULL AND t.mes = :mes)) AND "
                + "( :anio IS NULL OR (:anio IS NOT NULL AND t.anio = :anio)) "
                + "ORDER BY t.anio ASC, t.mes ASC")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TControlAsistMensual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDCONTROLASISTMENSUAL")
    private Long idcontrolasistmensual;
    @Column(name = "IDTESTADO")
    private Integer idtestado;
    @Column(name = "IDTMES")
    private Integer idtmes;
    @Size(max = 30)
    @Column(name = "DOCUMENTOIDENTIDAD")
    private String documentoidentidad;
    @Column(name = "MES")
    private Integer mes;
    @Column(name = "ANIO")
    private Integer anio;
    @Column(name = "FECHAPROCESO")
    @Temporal(TemporalType.DATE)
    private Date fechaproceso;
    @Column(name = "FECHADESDE")
    @Temporal(TemporalType.DATE)
    private Date fechadesde;
    @Column(name = "FECHAHASTA")
    @Temporal(TemporalType.DATE)
    private Date fechahasta;
    @Column(name = "MINUTOSTARDANZAPLANILLA")
    private Integer minutostardanzaplanilla;
    @Column(name = "MINUTOSTARDANZA")
    private Integer minutostardanza;
    @Column(name = "MINUTOSEXTRASALIDA")
    private Integer minutosextrasalida;
    @Column(name = "MINUTOSFALTASALIDA")
    private Integer minutosfaltasalida;
    @Column(name = "HORASTRABAJADASREMOTO")
    private Integer horastrabajadasremoto;
    @Column(name = "HORASTRABAJADASPRESENCIAL")
    private Integer horastrabajadaspresencial;
    @Column(name = "HORASADICIONALTRABAJO")
    private Integer horasadicionaltrabajo;
    @Column(name = "HORASTRABAJADAS")
    private Integer horastrabajadas;
    @Column(name = "HORASTOTALATRABAJAR")
    private Integer horastotalatrabajar;
    @Column(name = "HORASTOTALRESTANTE")
    private Integer horastotalrestante;
    @Column(name = "DIASATRABAJAR")
    private Integer diasatrabajar;
    @Column(name = "DIASTRABAJADAS")
    private Integer diastrabajadas;
    @Column(name = "DIASFALTA")
    private Integer diasfalta;
    @Column(name = "DIASRESTANTE")
    private Integer diasrestante;
    @Column(name = "MINUTOSPERMISOCONGOCE")
    private Integer minutospermisocongoce;
    @Column(name = "MINUTOSPERMISOSINGOCE")
    private Integer minutospermisosingoce;
    @Column(name = "HORASLICENCIACONGOCE")
    private Integer horaslicenciacongoce;
    @Column(name = "HORASLICENCIASINGOCE")
    private Integer horaslicenciasingoce;
    @Column(name = "HORASCOMISIONDESERVICIO")
    private Integer horascomisiondeservicio;
    @Column(name = "MINUTOSSALUD")
    private Integer minutossalud;
    @Column(name = "HORASVACACIONES")
    private Integer horasvacaciones;
    @Column(name = "HORASCAPACITACION")
    private Integer horascapacitacion;
    @Column(name = "DIASFERIADOCOMPENSABLE")
    private Integer diasferiadocompensable;
    @Column(name = "DIASFERIADONOCOMPENSABLE")
    private Integer diasferiadonocompensable;
    @Lob
    @Size(max = 65535)
    @Column(name = "DOCUMENTOSJSON")
    private String documentosjson;
    @Lob
    @Size(max = 65535)
    @Column(name = "CALENDARIOJSON")
    private String calendariojson;
    @Column(name = "FLAGMODIFICACION")
    private Integer flagmodificacion;
    @Size(max = 400)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Column(name = "IDUSUARIOCREACION")
    private Integer idusuariocreacion;
    @Column(name = "IDUSUARIOMODIFICACION")
    private Integer idusuariomodificacion;
    @Column(name = "FECHACREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacreacion;
    @Column(name = "FECHAMODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechamodificacion;
    @Column(name = "ESTADO")
    private Integer estado;
    @JoinColumn(name = "IDPERSONAL", referencedColumnName = "IDPERSONAL")
    @ManyToOne
    private TPersonal idpersonal;
}
