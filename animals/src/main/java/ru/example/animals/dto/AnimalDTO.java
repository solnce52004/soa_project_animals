package ru.example.animals.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;
import ru.example.animals.entity.Animal;
import ru.example.animals.entity.AnimalType;
import ru.example.animals.enums.GenderType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class AnimalDTO implements Serializable {
    @Transient
    private static final long serialVersionUID = 112L;

    public static final String ERROR_MSG_EMPTY_VALUE = "Empty value";
    public static final String ERROR_MSG_NOT_VALID = "Invalid value";

    @JsonIgnore
    private Long id;
    private String username;
    private AnimalTypeDTO animalType = new AnimalTypeDTO();

    @ApiModelProperty(notes = "Animal name", required = true)
    @NotBlank(message = ERROR_MSG_EMPTY_VALUE)
    @Size(min = 2, max = 255, message = ERROR_MSG_NOT_VALID)
    private String name;
    private GenderType gender = GenderType.UNTITLED;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date birthdate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalDTO animalDTO = (AnimalDTO) o;
        return getUsername().equals(animalDTO.getUsername()) &&
                getName().equals(animalDTO.getName()) &&
                getGender() == animalDTO.getGender() &&
                Objects.equals(getBirthdate(), animalDTO.getBirthdate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getName(), getGender(), getBirthdate());
    }

    @Override
    public String toString() {
        return "AnimalDTO{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", birthdate=" + birthdate +
                '}';
    }

    public static Animal dtoMapToExistsAnimal(AnimalDTO animalDTO, Animal animal) {
        final AnimalTypeDTO animalType = animalDTO.getAnimalType();
        return animal
                .setId(animalDTO.getId())
                .setUsername(animalDTO.getUsername())
                .setAnimalType(AnimalTypeDTO.dtoMapToAnimalType(animalType))
                .setName(animalDTO.getName())
                .setGender(animalDTO.getGender())
                .setBirthdate(animalDTO.getBirthdate());
    }

    public static Animal dtoMapToAnimal(AnimalDTO animalDTO) {
        return new Animal()
                .setId(animalDTO.getId())
                .setUsername(animalDTO.getUsername())
                .setName(animalDTO.getName())
                .setGender(animalDTO.getGender())
                .setBirthdate(animalDTO.getBirthdate());
    }

    public static AnimalDTO animalMapToDto(Animal animal) {
        final AnimalType animalType = animal.getAnimalType();
        return new AnimalDTO()
                .setId(animal.getId())
                .setUsername(animal.getUsername())
                .setAnimalType(AnimalTypeDTO.animalTypeMapToDto(animalType))
                .setName(animal.getName())
                .setGender(animal.getGender())
                .setBirthdate(animal.getBirthdate());
    }
}
