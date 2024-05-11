package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.JwtResponseDto;
import antisocial.app.backend.data.dto.RegisterLoginDto;

public interface IUserService {

    void registerNewUser(RegisterLoginDto registerLoginDto);

    JwtResponseDto login(RegisterLoginDto registerLoginDto);

}
