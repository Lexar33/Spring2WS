package spring2ws.exception;

public class DBException extends BaseException{

    private static final long serialVersionUID = 1L;

    public DBException(String message, Exception exception) {
        super(message, exception);
    }

    public DBException(String code, String message) {
        super(code, message);
    }

    public DBException(String code, String message, Exception exception) {
        super(code, message, exception);
    }

    public DBException(Exception exception) {
        super(exception);
    }

    public DBException(String message) {
        super(message);
    }
}
