package ru.example.auth.dto;

import lombok.Data;

@Data
final public class AuthRequestDTO {
    private String username;
    private String password;
}
