package antisocial.app.backend.errorHandling.exception;

public class FriendRequestException extends RuntimeException{
    public FriendRequestException(String message) {
        super(message);
    }
}
