package antisocial.app.backend.controller;

import antisocial.app.backend.component.CatchException;
import antisocial.app.backend.data.dto.JwtResponseDto;
import antisocial.app.backend.data.dto.RegisterLoginDto;
import antisocial.app.backend.data.dto.ResponseMessageDto;
import antisocial.app.backend.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ResponseMessageDto> register(@RequestBody RegisterLoginDto registerLoginDto){
        try {
            userService.registerNewUser(registerLoginDto);

            ResponseMessageDto responseMessageDto = new ResponseMessageDto("User Registered");

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessageDto);

        } catch (Exception exception) {

            return catchException.catchException(exception, exception.getMessage(), HttpStatus.BAD_REQUEST, this.getClass());
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody RegisterLoginDto registerLoginDto){
        try{
            JwtResponseDto jwtResponseDto = userService.login(registerLoginDto);

            return ResponseEntity.ok(jwtResponseDto);

        } catch (Exception exception){

            return catchException.catchException(exception, "Invalid Password or/and username",
                    HttpStatus.UNAUTHORIZED, this.getClass());
        }
    }
}
