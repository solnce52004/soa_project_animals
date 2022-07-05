package ru.example.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Data
final public class RoleDTO {
    private String title;
    private Set<String> permissions = new HashSet<>();
}
