package ru.example.auth.service.by_entities;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.auth.entity.Role;
import ru.example.auth.repo.RoleRepository;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getRoleByTitle(String title) {
        return roleRepository.getRoleByTitle(title).orElse(null);
    }
}
