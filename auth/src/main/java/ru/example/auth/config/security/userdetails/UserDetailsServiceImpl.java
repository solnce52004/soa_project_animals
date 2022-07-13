package ru.example.auth.config.security.userdetails;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.example.auth.entity.User;
import ru.example.auth.exception.custom_exception.UserNotFoundException;
import ru.example.auth.service.by_entities.UserService;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        final User user = userService
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        return UserDetailsImpl.create(user);
    }
}
