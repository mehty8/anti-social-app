package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.IResponseDto;
import antisocial.app.backend.data.dto.JwtResponseDto;
import antisocial.app.backend.data.dto.RegisterLoginDto;

import java.util.concurrent.CompletableFuture;

public interface IUserService {

    CompletableFuture<Void> registerNewUser(RegisterLoginDto registerLoginDto);

    CompletableFuture<IResponseDto> login(RegisterLoginDto registerLoginDto);

}
