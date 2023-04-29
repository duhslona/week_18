package my.backend.library.service;

import lombok.RequiredArgsConstructor;
import my.backend.library.model.User;
import my.backend.library.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElseThrow();
        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " was not found");
        }
        return user;
    }
}
