package spring2ws.proxy;

import spring2ws.dto.VwPersonalDto;

import java.util.List;

public interface PersonalWS {

    public List<VwPersonalDto> listarPersonalFechaHasta(String idpersona, String idpersonal, String iddependencias, String idttipocontrato, String idtestado, String documentoidentidad, String fechahasta, String tipoconsulta);
}

