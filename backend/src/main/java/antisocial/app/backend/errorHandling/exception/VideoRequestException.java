package antisocial.app.backend.errorHandling.exception;

public class VideoRequestException extends RuntimeException{
    public VideoRequestException(String message) {
        super(message);
    }
}
