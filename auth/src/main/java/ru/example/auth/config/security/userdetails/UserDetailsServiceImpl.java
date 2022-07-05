package ru.example.auth.config.security.userdetails;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.example.auth.entity.User;
import ru.example.auth.exception.custom_exception.UserNotFoundException;
import ru.example.auth.service.by_entities.UserService;

@Log4j2
@Service("userDetailsServiceImpl")
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        final User user = userService
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        return UserDetailsImpl.create(user);
    }
}
