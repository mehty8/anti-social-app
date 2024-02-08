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

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessageDto("User Registered"));

        } catch (Exception ex){
            logger.error(ex.getMessage());

            String responseMes = ex instanceof RegisterException
                    ? ex.getMessage()
                    : "Sorry, something went wrong, try again please";
            ResponseMessageDto responseMessageDto = new ResponseMessageDto(responseMes);

            return ex instanceof RegisterException
                    ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessageDto)
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessageDto);
        }
    }
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        try{
            JwtResponseDto jwtResponseDto = userService.login(loginDto);

            return ResponseEntity.ok(jwtResponseDto);

        } catch (Exception ex){
            logger.error(ex.getMessage());

            String responseMes = ex instanceof BadCredentialsException
                    ? "Invalid Password or/and username"
                    : "Sorry, something went wrong, try again please";
            ResponseMessageDto responseMessageDto = new ResponseMessageDto(responseMes);

            return ex instanceof BadCredentialsException
                    ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessageDto)
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessageDto);
        }
    }
}
