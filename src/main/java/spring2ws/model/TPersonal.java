package spring2ws.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "T_PERSONAL")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TPersonal.findAll", query = "SELECT t FROM TPersonal t")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDPERSONAL")
    private Integer idpersonal;
    @Column(name = "IDTESTADO")
    private Integer idtestado;
    @Column(name = "IDTGRADOINSTRUCCION")
    private Integer idtgradoinstruccion;
    @Column(name = "IDTNIVELINSTRUCCION")
    private Integer idtnivelinstruccion;
    @Column(name = "IDTTIPOPERSONAL")
    private Integer idttipopersonal;
    @Column(name = "IDTCONFIANZA")
    private Integer idtconfianza;
    @Column(name = "IDTSISTEMAPENSION")
    private Integer idtsistemapension;
    @Column(name = "IDTTIPOSEGUROSALUD")
    private Integer idttiposegurosalud;
    @Column(name = "IDTENTIDADBANCARIA")
    private Integer idtentidadbancaria;
    @Column(name = "IDTTIPOASISTENCIA")
    private Integer idttipoasistencia;
    @Size(max = 100)
    @Column(name = "URLIMAGEN")
    private String urlimagen;
    @Size(max = 30)
    @Column(name = "RUC")
    private String ruc;
    @Size(max = 80)
    @Column(name = "TITULOPROFESIONAL")
    private String tituloprofesional;
    @Size(max = 20)
    @Column(name = "NROCELULARPNSU")
    private String nrocelularpnsu;
    @Size(max = 20)
    @Column(name = "NROANEXOPNSU")
    private String nroanexopnsu;
    @Size(max = 50)
    @Column(name = "ENTIDADBANCARIA")
    private String entidadbancaria;
    @Size(max = 25)
    @Column(name = "NROCTACORRIENTE")
    private String nroctacorriente;
    @Size(max = 25)
    @Column(name = "NROCCI")
    private String nrocci;
    @Size(max = 50)
    @Column(name = "SISTEMAPENSION")
    private String sistemapension;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "DESCUENTOCAFAE")
    private Double descuentocafae;
    @Column(name = "DESCUENTOCUSSP")
    private Double descuentocussp;
    @Size(max = 100)
    @Column(name = "CORREOINSTITUCIONAL")
    private String correoinstitucional;
    @Column(name = "EXONERADOMARCAR")
    private Integer exoneradomarcar;
    @Column(name = "FECHAINICIOPNSU")
    @Temporal(TemporalType.DATE)
    private Date fechainiciopnsu;
    @Lob
    @Size(max = 65535)
    @Column(name = "FUNCIONES")
    private String funciones;
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
    @Size(max = 300)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Column(name = "ESTADO")
    private Integer estado;
    @OneToMany(mappedBy = "idpersonal")
    private List<TControlAsistMensual> tControlAsistMensualList;
    @OneToMany(mappedBy = "idpersonal")
    private List<TControlAsistParcial> tControlAsistParcialList;

}
