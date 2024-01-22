package antisocial.app.backend.controller;

import antisocial.app.backend.data.dto.JwtStringDto;
import antisocial.app.backend.data.dto.LoginDto;
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

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(@RequestBody LoginDto loginDto){
        userService.addNewUser(loginDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("login")
    public ResponseEntity<JwtStringDto> login(@RequestBody LoginDto loginDto){
        JwtStringDto jwtStringDto = userService.login(loginDto);
        return ResponseEntity.ok(jwtStringDto);
    }
}
