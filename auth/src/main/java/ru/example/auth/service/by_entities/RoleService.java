package ru.example.auth.service.by_entities;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.auth.entity.Permission;
import ru.example.auth.entity.Role;
import ru.example.auth.entity.User;
import ru.example.auth.enums.PermissionEnum;
import ru.example.auth.enums.RoleEnum;
import ru.example.auth.repo.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    public Role getRoleByTitle(String title) {
        return roleRepository.getRoleByTitle(title).orElse(null);
    }

    public User addRolePermissionUser(User user) {
        Permission permission = permissionService.getPermissionByTitle(PermissionEnum.WRITE.name());
        Set<Permission> permissions = new HashSet<>();
        permissions.add(permission);

        Role role = this.getRoleByTitle(RoleEnum.ROLE_USER.name());
        role.setPermissions(permissions);

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);

        return user;
    }
}
