package security;

import entity.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reprository.UserRepository;

@Service
@Transactional(readOnly = true)
public class SecurityUserDetailsService implements UserDetailsService {
    @Autowired
    UserDTO userDTO = null;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDetails userDTO = UserRepository.login(login);
         return userDTO;
    }

    public void setUserDto(UserDTO userDto) {
        this.userDTO = userDto;
    }
}
