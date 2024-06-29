package antisocial.app.backend.controller;

import antisocial.app.backend.data.dto.IResponseDto;
import antisocial.app.backend.errorHandling.exception.component.CatchException;
import antisocial.app.backend.data.dto.RegisterLoginDto;
import antisocial.app.backend.data.dto.ResponseMessageDto;
import antisocial.app.backend.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user")
public class UserController {

    private IUserService userService;

    private CatchException catchException;


    public UserController(IUserService userService, CatchException catchException) {
        this.userService = userService;
        this.catchException = catchException;
    }


    @PostMapping("register")
    public CompletableFuture<ResponseEntity<IResponseDto>> register(@RequestBody RegisterLoginDto registerLoginDto){

        return userService.registerNewUser(registerLoginDto).thenApply(voided -> {

            IResponseDto responseMessageDto = new ResponseMessageDto("User Registered");

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessageDto);

        }).exceptionally(exception
                -> catchException.catchException((Exception) exception, exception.getMessage(),
                HttpStatus.BAD_REQUEST, this.getClass()));

    }

    @PostMapping("login")
    public CompletableFuture<ResponseEntity<IResponseDto>> login(@RequestBody RegisterLoginDto registerLoginDto){

        return userService.login(registerLoginDto).thenApply(jwtResponseDto
                -> ResponseEntity.ok(jwtResponseDto)).exceptionally(exception
                -> catchException.catchException((Exception) exception, "Invalid Password or/and username",
                HttpStatus.UNAUTHORIZED, this.getClass()));

    }
}
