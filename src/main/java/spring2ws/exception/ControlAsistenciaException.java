package spring2ws.exception;

public class ControlAsistenciaException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ControlAsistenciaException(String message, Exception exception) {
        super(message, exception);
    }

    public ControlAsistenciaException(String code, String message) {
        super(code, message);
    }

    public ControlAsistenciaException(String code, String message, Exception exception) {
        super(code, message, exception);
    }

    public ControlAsistenciaException(Exception exception) {
        super(exception);
    }

    public ControlAsistenciaException(String message) {
        super(message);
    }
}