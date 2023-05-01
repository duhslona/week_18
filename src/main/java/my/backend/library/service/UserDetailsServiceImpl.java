package my.backend.library.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.backend.library.dto.RoleDto;
import my.backend.library.dto.UserDto;
import my.backend.library.model.User;
import my.backend.library.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Try to find user by username {}.", username);
        Optional<User> user = userRepository.findByLogin(username);
        if (user.isPresent()) {
            UserDto userDto = convertToDto(user.get());
            log.info("User: {}.", userDto);
            return userDto;
        } else {
            log.error("Can't find user with username {}.", username);
            throw new UsernameNotFoundException("User with username " + username + " was not found");
        }
    }

    private UserDto convertToDto(User user) {
        List<RoleDto> roleDtoList = user.getRoles().stream()
                .map(role -> RoleDto.builder()
                        .name(role.getName())
                        .build()).toList();
        UserDto result = UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .password(user.getPassword())
                .roles(roleDtoList)
                .build();

        return result;
    }
}
