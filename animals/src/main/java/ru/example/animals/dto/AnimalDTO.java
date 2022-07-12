package ru.example.animals.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import ru.example.animals.entity.Animal;
import ru.example.animals.entity.AnimalType;
import ru.example.animals.enums.GenderType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public final class AnimalDTO implements Serializable {
    private static final long serialVersionUID = -805829958200586923L;

    public static final String ERROR_MSG_EMPTY_VALUE = "Empty value";
    public static final String ERROR_MSG_NOT_VALID = "Invalid value";

    private Long id;
    private String username;
    private AnimalTypeDTO animalType = new AnimalTypeDTO();

    @ApiModelProperty(notes = "Animal name", required = true)
    @NotBlank(message = ERROR_MSG_EMPTY_VALUE)
    @Size(min = 2, max = 255, message = ERROR_MSG_NOT_VALID)
    private String animalName;
    private GenderType gender = GenderType.UNTITLED;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date birthdate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalDTO animalDTO = (AnimalDTO) o;
        return Objects.equals(getId(), animalDTO.getId()) &&
                getUsername().equals(animalDTO.getUsername()) &&
                Objects.equals(getAnimalType(), animalDTO.getAnimalType()) &&
                getAnimalName().equals(animalDTO.getAnimalName()) &&
                getGender() == animalDTO.getGender() &&
                Objects.equals(getBirthdate(), animalDTO.getBirthdate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getUsername(),
                getAnimalType(),
                getAnimalName(),
                getGender(),
                getBirthdate());
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", animalType=" + animalType +
                ", animalName='" + animalName + '\'' +
                ", gender=" + gender +
                ", birthdate=" + birthdate +
                '}';
    }

    public static Animal animalMapToNewAnimal(Animal old) {
        return new Animal()
                .setId(old.getId())
                .setUsername(old.getUsername())
                .setAnimalName(old.getAnimalName())
                .setAnimalType(old.getAnimalType())
                .setGender(old.getGender())
                .setBirthdate(old.getBirthdate())
                .setUpdatedAt(old.getUpdatedAt())
                .setCreatedAt(old.getCreatedAt());
    }

    public static Animal dtoMapToNewAnimal(AnimalDTO dto) {
        final GenderType gender = GenderType.getOrDefaultGenderName(dto.getGender());

        return new Animal()
                .setId(dto.getId())
                .setUsername(dto.getUsername())
                .setAnimalName(dto.getAnimalName())
                .setGender(gender)
                .setBirthdate(dto.getBirthdate());
    }

    public static AnimalDTO animalMapToDto(Animal a) {
        final AnimalType type = a.getAnimalType();
        final AnimalTypeDTO mapType = AnimalTypeDTO.animalTypeMapToDto(type);
        final GenderType gender = GenderType.getOrDefaultGenderName(a.getGender());

        return new AnimalDTO()
                .setId(a.getId())
                .setUsername(a.getUsername())
                .setAnimalType(mapType)
                .setAnimalName(a.getAnimalName())
                .setGender(gender)
                .setBirthdate(a.getBirthdate());
    }
}
