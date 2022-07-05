package ru.example.auth.service.by_entities;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.auth.entity.Permission;
import ru.example.auth.repo.PermissionRepository;

@Service
@AllArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Permission getPermissionByTitle(String title) {
        return permissionRepository.getPermissionByTitle(title).orElse(null);
    }
}
