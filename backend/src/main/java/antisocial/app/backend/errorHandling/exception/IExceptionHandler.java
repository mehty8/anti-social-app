package antisocial.app.backend.errorHandling.exception;

import antisocial.app.backend.data.dto.ResponseMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface IExceptionHandler {
    boolean isNeeded(Exception exception);
    ResponseEntity<ResponseMessageDto> handleException(Exception exception, String message,
                                                       HttpStatus httpStatus, Class<?> clazz);
}
