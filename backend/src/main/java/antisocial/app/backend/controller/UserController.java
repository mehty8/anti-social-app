package antisocial.app.backend.controller;

import antisocial.app.backend.data.dto.JwtResponseDto;
import antisocial.app.backend.data.dto.LoginDto;
import antisocial.app.backend.data.dto.ResponseMessageDto;
import antisocial.app.backend.errorHandling.exception.RegisterException;
import antisocial.app.backend.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseEntity<ResponseMessageDto> register(@RequestBody LoginDto loginDto){
        try {
            userService.registerNewUser(loginDto);
            ResponseMessageDto responseMessage = new ResponseMessageDto("User Registered");
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
        } catch (Exception ex){
            logger.error(ex.getMessage());
            ResponseMessageDto responseMessage = new ResponseMessageDto(ex.getMessage());
            return ex instanceof RegisterException ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage)
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        try{
            JwtResponseDto jwtResponseDto = userService.login(loginDto);
            return ResponseEntity.ok(jwtResponseDto);
        } catch (Exception ex){
            return ex instanceof BadCredentialsException
                    ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessageDto("Invalid Password or/and username"))
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessageDto("Sorry, something went wrong, try again please"));
        }
    }
}
