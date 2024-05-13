package antisocial.app.backend.errorHandling.exception;

import antisocial.app.backend.data.dto.ResponseMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestExceptionHandler implements IExceptionHandler{

    @Override
    public boolean isNeeded(Exception exception) {
        return exception instanceof FriendRequestException;
    }

    @Override
    public ResponseEntity<ResponseMessageDto> handleException(Exception exception, String message,
                                                              HttpStatus httpStatus, Class<?> clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.error(exception.getMessage());

        ResponseMessageDto responseMessageDto = new ResponseMessageDto(message);

        return ResponseEntity.status(httpStatus).body(responseMessageDto);
    }
}
