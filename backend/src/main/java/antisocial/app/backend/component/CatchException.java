package antisocial.app.backend.component;

import antisocial.app.backend.data.dto.ResponseMessageDto;
import antisocial.app.backend.errorHandling.exception.IExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CatchException {

    private List<IExceptionHandler> exceptionHandlers;

    public CatchException(List<IExceptionHandler> exceptionHandlers) {
        this.exceptionHandlers = exceptionHandlers;
    }

    public ResponseEntity<ResponseMessageDto> catchException(Exception exception, String message,
                                                             HttpStatus httpStatus, Class<?> clazz){
        for(IExceptionHandler exceptionHandler : exceptionHandlers){
            if(exceptionHandler.isNeeded(exception)){
                return exceptionHandler.handleException(exception, message, httpStatus, clazz);
            }
        }

        message = "Sorry, something went wrong, try again please";
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        ResponseMessageDto responseMessageDto = new ResponseMessageDto(message);

        return ResponseEntity.status(httpStatus).body(responseMessageDto);
    }
}
