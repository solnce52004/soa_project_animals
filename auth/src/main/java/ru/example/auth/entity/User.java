package ru.example.auth.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true)
@Data
public class User {
    private Long id;
    private String username;
}
