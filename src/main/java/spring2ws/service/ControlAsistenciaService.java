package spring2ws.service;

import spring2ws.util.RestResponse;

import java.util.List;

public interface ControlAsistenciaService {

    public RestResponse registrarcontrolasistenciaparcial(String idpersonal, String documentoidentidad,String desde, String hasta);

}
