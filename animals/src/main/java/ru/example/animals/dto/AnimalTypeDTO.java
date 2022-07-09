package ru.example.animals.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.example.animals.entity.AnimalType;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Accessors(chain = true)
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class AnimalTypeDTO implements Serializable {
    private static final long serialVersionUID = 2805268199853829209L;

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

    public static AnimalTypeDTO animalTypeMapToDto(AnimalType animalType) {
        return new AnimalTypeDTO()
                .setId(animalType.getId())
                .setTitle(animalType.getTitle());
    }
}

