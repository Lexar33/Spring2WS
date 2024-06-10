package spring2ws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import spring2ws.dto.RequestParcialDto;
import spring2ws.service.ControlAsistenciaService;
import spring2ws.util.Constantes;
import spring2ws.util.RestResponse;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class UsuarioController {

    Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    ControlAsistenciaService oControlAsistenciaService;

    @RequestMapping(value = "/registrarcontrolasistenciaparcial", method = RequestMethod.POST,consumes = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    Map<String, Object> registrarcontrolasistenciaparcial(@Valid @RequestBody RequestParcialDto oRequestDto) {

        logger.info("--------------------------------------------------------------------------");
        logger.info("============ INICIAR REGISTRAR ASISTENCIA PARCIAL =============");

        Map<String, Object> map = new HashMap<>();

        RestResponse rpta = oControlAsistenciaService.registrarcontrolasistenciaparcial(
                Optional.ofNullable(oRequestDto.getIdpersonal()).isPresent() ? oRequestDto.getIdpersonal() : "",
                Optional.ofNullable(oRequestDto.getDocumentoidentidad()).isPresent() ? oRequestDto.getDocumentoidentidad() : "",
                oRequestDto.getDesde(), oRequestDto.getHasta());

        map.put(Constantes.VALUE, rpta.getObject());

        if (Constantes.CODIGO_RPSTA_OK.equals(rpta.getCodigo())) {
            map.put(Constantes.STATUS, Constantes.CODIGO_RPSTA_OK);
            map.put(Constantes.MESSAGE, Constantes.RPSTA_OK);
        } else {
            map.put(Constantes.STATUS, rpta.getCodigo());
            map.put(Constantes.MESSAGE, rpta.getMensaje());
        }

        logger.info("=============== FIN REGISTRAR ASISTENCIA PARCIAL ==============");
        logger.info("--------------------------------------------------------------------------");

        return map;
    }


    @RequestMapping(value = "/prueba", method = RequestMethod.GET)
    public @ResponseBody
    String welcome() {
        return "API REST Spring2WS";
    }


}
