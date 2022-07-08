package ru.example.animals.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;
import ru.example.animals.entity.AnimalType;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class AnimalTypeDTO implements Serializable {
    @Transient
    private static final long serialVersionUID = 115L;

    @JsonIgnore
    private Long id;
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalTypeDTO that = (AnimalTypeDTO) o;
        return getTitle().equals(that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle());
    }

    public AnimalTypeDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static AnimalType dtoMapToAnimalType(AnimalTypeDTO type) {
        return new AnimalType()
                .setId(type.getId())
                .setTitle(type.getTitle());
    }

    public static AnimalTypeDTO animalTypeMapToDto(AnimalType type) {
        return new AnimalTypeDTO(
                type.getId(),
                type.getTitle()
        );
    }
}

