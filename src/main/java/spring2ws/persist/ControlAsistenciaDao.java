package spring2ws.persist;

import spring2ws.exception.DBException;
import spring2ws.model.TControlAsistParcial;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ControlAsistenciaDao {


    public List<TControlAsistParcial> listControlAsistenciaParcial(String idpersonal, String documentoidentidad, String desde, String hasta) throws DBException, ParseException;

    public List<TControlAsistParcial> listControlAsistenciaParcial(String idpersonal, String documentoidentidad, int mes, int anio) throws DBException;

}
