package spring2ws.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestResponse {
    private String codigo;
    private String mensaje;
    private Object object;
}
