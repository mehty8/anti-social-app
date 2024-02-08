package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.JwtResponseDto;
import antisocial.app.backend.data.dto.LoginDto;
import antisocial.app.backend.data.entity.RoleEntity;
import antisocial.app.backend.data.entity.UserEntity;
import antisocial.app.backend.errorHandling.exception.RegisterException;
import antisocial.app.backend.repository.IRoleRepository;
import antisocial.app.backend.repository.IUserRepository;
import antisocial.app.backend.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{
    private IUserRepository userRepository;

    private IRoleRepository roleRepository;

    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserService(IUserRepository userRepository, IRoleRepository roleRepository,
                       PasswordEncoder encoder, AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void registerNewUser(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        checkUsername(username);
        checkPassword(password);

        String passwordEncoded = encoder.encode(password);

        RoleEntity role = roleRepository.findByRoleName("User").get();

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoded);
        userEntity.addRole(role);

        userRepository.save(userEntity);
    }

    @Override
    public JwtResponseDto login(LoginDto loginDto){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        JwtResponseDto jwtResponseDto = new JwtResponseDto(jwt);

        return jwtResponseDto;
    }

    private void checkUsername(String username){
        if(userRepository.findByUsername(username).isPresent()){
            throw new RegisterException("Username is already taken!");
        } else if(!username.matches("\\w+")){
            throw new RegisterException("Username can only have letters, numbers and underscore");
        }
    }

    private void checkPassword(String password){
        if(!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")){
            throw new RegisterException("Password must be at least 8 characters long and have" +
                    " at least 1 uppercase, 1 lowercase and 1 digit!");
        }
    }
}

//Just an example
/*
@Async
    @Override
    public CompletableFuture<Void> addNewUser(LoginDto loginDto) {
        return CompletableFuture.runAsync(() -> {
            RoleEntity role = roleRepository.findByRoleName("User").get();

            String username = loginDto.getUsername();
            String password = encoder.encode(loginDto.getPassword());

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setPassword(password);
            userEntity.addRole(role);

            userRepository.save(userEntity);
        });
    }*/
