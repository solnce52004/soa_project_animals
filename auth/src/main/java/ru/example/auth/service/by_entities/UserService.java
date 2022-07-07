package ru.example.auth.service.by_entities;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.auth.entity.Permission;
import ru.example.auth.entity.Role;
import ru.example.auth.entity.User;
import ru.example.auth.enums.PermissionEnum;
import ru.example.auth.enums.RoleEnum;
import ru.example.auth.repo.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PermissionService permissionService;

    public void save(User obj) {
        userRepository.save(obj);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void createUser(User user) {
        Permission permission = permissionService.getPermissionByTitle(PermissionEnum.WRITE.name());
        Set<Permission> permissions = new HashSet<>();
        permissions.add(permission);

        Role role = roleService.getRoleByTitle(RoleEnum.ROLE_USER.name());
        role.setPermissions(permissions);

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);

        save(user);
    }
}
