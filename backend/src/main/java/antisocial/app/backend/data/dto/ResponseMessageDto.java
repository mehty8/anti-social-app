package antisocial.app.backend.data.dto;

public class ResponseMessageDto {
    private String message;

    public ResponseMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
