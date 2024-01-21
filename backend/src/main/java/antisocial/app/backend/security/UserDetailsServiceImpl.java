package antisocial.app.backend.security;

import antisocial.app.backend.data.entity.RoleEntity;
import antisocial.app.backend.data.entity.UserEntity;
import antisocial.app.backend.repository.IUserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final IUserRepository userRepository;

    public UserDetailsServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        for (RoleEntity role : userEntity.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return new User(userEntity.getUsername(), userEntity.getPassword(), roles);
    }
}
