package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.JwtStringDto;
import antisocial.app.backend.data.dto.LoginDto;

public interface IUserService {

    void addNewUser(LoginDto loginDto);

    JwtStringDto login(LoginDto loginDto);
}
