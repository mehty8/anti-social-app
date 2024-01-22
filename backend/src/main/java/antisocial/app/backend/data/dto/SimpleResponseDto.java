package antisocial.app.backend.data.dto;

public class SimpleResponseDto {
    private String message;

    public SimpleResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
