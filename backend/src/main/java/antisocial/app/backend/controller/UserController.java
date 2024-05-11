package antisocial.app.backend.controller;

import antisocial.app.backend.data.dto.JwtResponseDto;
import antisocial.app.backend.data.dto.RegisterLoginDto;
import antisocial.app.backend.data.dto.ResponseMessageDto;
import antisocial.app.backend.errorHandling.exception.RegisterException;
import antisocial.app.backend.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private IUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    public UserController(IUserService userService) {
        this.userService = userService;
    }


    @PostMapping("register")
    public ResponseEntity<ResponseMessageDto> register(@RequestBody RegisterLoginDto registerLoginDto){
        try {
            userService.registerNewUser(registerLoginDto);

            ResponseMessageDto responseMessageDto = new ResponseMessageDto("User Registered");

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessageDto);

        } catch (Exception exception){
            logger.error(exception.getMessage());

            String responseMes = exception instanceof RegisterException
                    ? exception.getMessage()
                    : "Sorry, something went wrong, try again please";
            ResponseMessageDto responseMessageDto = new ResponseMessageDto(responseMes);

            return exception instanceof RegisterException
                    ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessageDto)
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessageDto);
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody RegisterLoginDto registerLoginDto){
        try{
            JwtResponseDto jwtResponseDto = userService.login(registerLoginDto);

            return ResponseEntity.ok(jwtResponseDto);

        } catch (Exception exception){
            logger.error(exception.getMessage());

            String responseMes = exception instanceof BadCredentialsException
                    ? "Invalid Password or/and username"
                    : "Sorry, something went wrong, try again please";
            ResponseMessageDto responseMessageDto = new ResponseMessageDto(responseMes);

            return exception instanceof BadCredentialsException
                    ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessageDto)
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessageDto);
        }
    }
}
