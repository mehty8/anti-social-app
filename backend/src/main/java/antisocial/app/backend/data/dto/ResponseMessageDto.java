package antisocial.app.backend.data.dto;

public class ResponseMessageDto implements IResponseDto{

    private String message;

    public ResponseMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
