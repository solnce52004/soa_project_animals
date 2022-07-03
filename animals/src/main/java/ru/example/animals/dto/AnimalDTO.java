package ru.example.animals.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;
import ru.example.animals.entity.Animal;
import ru.example.animals.enums.GenderType;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class AnimalDTO implements Serializable {
    @JsonIgnore
    @Transient
    private static final long serialVersionUID = 112L;

    private Long id;
    private UserDTO user = new UserDTO();
    private AnimalTypeDTO animalType = new AnimalTypeDTO();
    private String name;
    private GenderType gender = GenderType.UNTITLED;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date birthdate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalDTO animal = (AnimalDTO) o;
        return getName().equals(animal.getName()) &&
                Objects.equals(getGender(), animal.getGender()) &&
                Objects.equals(getBirthdate(), animal.getBirthdate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getGender(), getBirthdate());
    }

    @Override
    public String toString() {
        return "AnimalDTO{" +
                "id=" + id +
                ", user=" + user +
                ", animalType=" + animalType +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", birthdate=" + birthdate +
                '}';
    }

    public AnimalDTO(
            Long id,
            UserDTO user,
            AnimalTypeDTO animalType,
            String name,
            GenderType gender,
            Date birthdate
    ) {
        this.id = id;
        this.user = user;
        this.animalType = animalType;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
    }

    public static AnimalDTO animalMapToDto(Animal animal) {
        return new AnimalDTO(
                animal.getId(),
                UserDTO.userMapToDto(animal.getUser()),
                AnimalTypeDTO.animalMapToDto(animal.getAnimalType()),
                animal.getName(),
                animal.getGender(),
                animal.getBirthdate()
        );
    }
}
