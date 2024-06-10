package spring2ws.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EAIUtil {


    public static SimpleDateFormat formatoFechaIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static SimpleDateFormat formatoFechaOut = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat formatoFechaBD = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat formatoHoraOut = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat formatoHoraIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");

    public static String obtenerNombreDia(Date fecha) {

        String[] dias = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};

        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);

        return dias[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }
}
