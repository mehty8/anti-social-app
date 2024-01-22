package antisocial.app.backend.data.dto;

public class JwtStringDto {
    private String jwt;

    public JwtStringDto(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
