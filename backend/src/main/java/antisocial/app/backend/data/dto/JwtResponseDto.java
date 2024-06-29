package antisocial.app.backend.data.dto;

public class JwtResponseDto implements IResponseDto{

    private String jwt;

    public JwtResponseDto(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
