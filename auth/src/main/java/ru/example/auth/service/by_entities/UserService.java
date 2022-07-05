package ru.example.auth.service.by_entities;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.auth.entity.Permission;
import ru.example.auth.entity.Role;
import ru.example.auth.entity.User;
import ru.example.auth.enums.PermissionEnum;
import ru.example.auth.enums.RoleEnum;
import ru.example.auth.enums.UserStatusEnum;
import ru.example.auth.repo.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PermissionService permissionService;

    public User save(User obj) {
        return userRepository.save(obj);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllByName(String username) {
        return userRepository.findAllByName(username);
    }

    //todo:!!!!
    public void patch(User obj) {
        userRepository.save(obj);
    }

    public User update(User obj) {
        return userRepository.save(obj);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User createAnonymousRead(User user) {
        Permission permission = permissionService.getPermissionByTitle(PermissionEnum.READ.name());
        Set<Permission> permissions = new HashSet<>();
        permissions.add(permission);

        Role role = roleService.getRoleByTitle(RoleEnum.ROLE_ANONYMOUS.name());
        role.setPermissions(permissions);

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setStatus(UserStatusEnum.NOT_CONFIRMED.name())
                .setRoles(roles);

        return save(user);
    }

    @Transactional
    public User createUserRead(User user) {
        Permission permission = permissionService.getPermissionByTitle(PermissionEnum.READ.name());
        Set<Permission> permissions = new HashSet<>();
        permissions.add(permission);

        Role role = roleService.getRoleByTitle(RoleEnum.ROLE_USER.name());
        role.setPermissions(permissions);

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);

        return save(user);
    }
}
