package antisocial.app.backend.errorHandling.exception;

public interface IExceptionFinder {
    boolean isNeeded(Exception exception);
}
