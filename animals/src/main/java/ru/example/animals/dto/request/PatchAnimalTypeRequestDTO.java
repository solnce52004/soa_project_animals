package ru.example.animals.dto.request;

import lombok.*;
import lombok.experimental.Accessors;
import ru.example.animals.dto.AnimalTypeDTO;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(exclude = {"animalType"})
@ToString(exclude = {"animalType"})
public class PatchAnimalTypeRequestDTO implements Serializable {
    private static final long serialVersionUID = -5863839403729099766L;

    private String username;
    private AnimalTypeDTO animalType = new AnimalTypeDTO();
}
