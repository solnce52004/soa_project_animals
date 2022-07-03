package ru.example.animals.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;
import ru.example.animals.entity.Animal;
import ru.example.animals.entity.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class UserDTO implements Serializable {
    @JsonIgnore
    @Transient
    private static final long serialVersionUID = 114L;

    private Long id;
    private String username;
    private Set<Animal> animals = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return getUsername().equals(userDTO.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    public UserDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserDTO(Long id, String username, Set<Animal> animals) {
        this.id = id;
        this.username = username;
        this.animals = animals;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    public static UserDTO userMapToDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername()
        );
    }

    public static UserDTO userMapToFullDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getAnimals()
        );
    }
}
