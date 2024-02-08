package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.JwtResponseDto;
import antisocial.app.backend.data.dto.LoginDto;

public interface IUserService {

    void registerNewUser(LoginDto loginDto);

    JwtResponseDto login(LoginDto loginDto);
}
