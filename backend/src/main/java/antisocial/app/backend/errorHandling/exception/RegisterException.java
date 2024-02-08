package antisocial.app.backend.errorHandling.exception;

public class RegisterException extends RuntimeException{
    public RegisterException(String message) {
        super(message);
    }
}
