package antisocial.app.frontend.dto;

public class RegisterLoginRequestDto {

    private String username;
    private String password;

    public RegisterLoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
