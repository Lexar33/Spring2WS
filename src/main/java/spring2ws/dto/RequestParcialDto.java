package spring2ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestParcialDto implements  Serializable{

   // private static final long serialVersionUID = 1L;

    private String idpersonal;
    private String documentoidentidad;
    @NotNull(message = "Ingresar la fecha desde")
    private String desde;
    @NotNull(message = "Ingresar la fecha hasta")
    private String hasta;


}
