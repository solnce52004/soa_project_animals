package ru.example.animals.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;
import ru.example.animals.entity.AnimalType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class AnimalTypeDTO implements Serializable {
    @JsonIgnore
    @Transient
    private static final long serialVersionUID = 115L;

    private Long id;
    private String title;
    private Set<AnimalDTO> animals = new HashSet<>();

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

    public static AnimalTypeDTO animalMapToDto(AnimalType animal) {
        return new AnimalTypeDTO(
                animal.getId(),
                animal.getTitle()
        );
    }
}

