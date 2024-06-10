package spring2ws.persist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spring2ws.dao.BaseDao;
import spring2ws.exception.DBException;
import spring2ws.model.TControlAsistParcial;
import spring2ws.util.EAIUtil;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class ControlAsistenciaPersist implements ControlAsistenciaDao{

    @Autowired
    BaseDao<Long, TControlAsistParcial> controlAsistParcialDAO;

    Logger logger = LoggerFactory.getLogger(ControlAsistenciaPersist.class);

    @Override
    public List<TControlAsistParcial> listControlAsistenciaParcial(String idpersonal, String documentoidentidad, String desde, String hasta) throws DBException, ParseException {
        logger.info(":::::::::::::::::::::::::: listControlAsistenciaParcial :::::::::::::::::::::::");
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("idpersonal", idpersonal.length() != 0 ? Integer.parseInt(idpersonal) : null);
        parametros.put("documentoidentidad", documentoidentidad.length() != 0 ? documentoidentidad : null);
        parametros.put("desde", (desde.length() != 0 ? EAIUtil.formatoFechaOut.parse(desde) : null));
        parametros.put("hasta", (hasta.length() != 0 ? EAIUtil.formatoFechaOut.parse(hasta) : null));
        return controlAsistParcialDAO.findByNamedQuery("TControlAsistParcial.findByPersonalDate", parametros);
    }

    @Override
    public List<TControlAsistParcial> listControlAsistenciaParcial(String idpersonal, String documentoidentidad, int mes, int anio) throws DBException {
        logger.info(":::::::::::::::::::::::::: listControlAsistenciaParcial :::::::::::::::::::::::");
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("idpersonal", idpersonal.length() != 0 ? Integer.parseInt(idpersonal) : null);
        parametros.put("documentoidentidad", documentoidentidad.length() != 0 ? documentoidentidad : null);
        parametros.put("mes", (Optional.ofNullable(mes).isPresent() ? mes : null));
        parametros.put("anio", (Optional.ofNullable(anio).isPresent() ? anio : null));
        return controlAsistParcialDAO.findByNamedQuery("TControlAsistParcial.findByPersonalMesAnio", parametros);
    }
}

