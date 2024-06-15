package antisocial.app.backend.errorHandling.exception.component;

import antisocial.app.backend.data.dto.ResponseMessageDto;
import antisocial.app.backend.errorHandling.exception.IExceptionFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CatchException {

    private List<IExceptionFinder> exceptionHandlers;

    public CatchException(List<IExceptionFinder> exceptionHandlers) {
        this.exceptionHandlers = exceptionHandlers;
    }

    public ResponseEntity<ResponseMessageDto> catchException(Exception exception, String message,
                                                             HttpStatus httpStatus, Class<?> controllerClass){
        for(IExceptionFinder exceptionHandler : exceptionHandlers){
            if(exceptionHandler.isNeeded(exception)){
                return handleException(exception, message, httpStatus, controllerClass);
            }
        }

        message = "Sorry, something went wrong, try again please";
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        ResponseMessageDto responseMessageDto = new ResponseMessageDto(message);

        return ResponseEntity.status(httpStatus).body(responseMessageDto);
    }

    private ResponseEntity<ResponseMessageDto> handleException(Exception exception, String message,
                                                               HttpStatus httpStatus, Class<?> controllerClass) {
        Logger logger = LoggerFactory.getLogger(controllerClass);
        logger.error(exception.getMessage());

        ResponseMessageDto responseMessageDto = new ResponseMessageDto(message);

        return ResponseEntity.status(httpStatus).body(responseMessageDto);
    }
}
