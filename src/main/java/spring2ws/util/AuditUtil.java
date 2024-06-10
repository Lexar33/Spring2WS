package spring2ws.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//@Data

public class AuditUtil {

    private static int idusuario;
    private static String token;
    private static String correoinstitucional;

    public static Date getFechaActual() {
        return new Date();
    }

    public static int getIdusuario() {
        return idusuario;
    }

    public static void setIdusuario(int aIdusuario) {
        idusuario = aIdusuario;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String aToken) {
        token = aToken;
    }

    public static String getCorreoinstitucional() {
        return correoinstitucional;
    }

    public static void setCorreoinstitucional(String aCorreoinstitucional) {
        correoinstitucional = aCorreoinstitucional;
    }

}
