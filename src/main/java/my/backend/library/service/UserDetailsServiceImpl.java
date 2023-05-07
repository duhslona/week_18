package my.backend.library.service;

import lombok.RequiredArgsConstructor;
import my.backend.library.dto.RoleDto;
import my.backend.library.dto.UserDto;
import my.backend.library.model.Role;
import my.backend.library.model.User;
import my.backend.library.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElseThrow();
        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " was not found");
        }
        return convertToDto(user);
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
