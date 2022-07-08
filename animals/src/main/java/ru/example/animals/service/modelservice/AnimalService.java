package ru.example.animals.service.modelservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.dto.AnimalTypeDTO;
import ru.example.animals.entity.Animal;
import ru.example.animals.entity.AnimalType;
import ru.example.animals.exception.custom_exception.AnimalNotFoundException;
import ru.example.animals.exception.custom_exception.AnimalTypeNotFoundException;
import ru.example.animals.exception.custom_exception.UserUnauthorizedException;
import ru.example.animals.repo.AnimalRepository;
import ru.example.animals.repo.AnimalTypeRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final AnimalTypeRepository animalTypeRepository;
    private final UserService userService;

    public AnimalDTO findAnimalById(AnimalDTO animalDTO) {
        final Animal animal = animalRepository.findById(animalDTO.getId())
                .orElseThrow(AnimalNotFoundException::new);

        final String animalUsername = animal.getUsername();
        final String requestUsername = animalDTO.getUsername();

        if (!userService.checkUserAnimal(animalUsername, requestUsername)) {
            throw new UserUnauthorizedException();
        }

        return AnimalDTO.animalMapToDto(animal);
    }

    public AnimalDTO create(AnimalDTO animalDTO) {
        final Animal toAnimal = AnimalDTO.dtoMapToAnimal(animalDTO);
        final String title = AnimalTypeDTO.dtoMapToAnimalType(animalDTO.getAnimalType()).getTitle();

        final AnimalType animalType = animalTypeRepository.findByTitle(title)
                .orElseThrow(AnimalTypeNotFoundException::new);

        final Animal animal = animalRepository.save(
                toAnimal.setAnimalType(animalType));
        return AnimalDTO.animalMapToDto(animal);
    }

    public AnimalDTO update(Long animalId, AnimalDTO animalDTO) {
        final Animal animal = animalRepository.findById(animalId)
                .orElseThrow(AnimalNotFoundException::new);

        if (!userService.checkUserAnimal(animal.getUsername(), animalDTO.getUsername())) {
            throw new UserUnauthorizedException();
        }

        final Animal updatedAnimal = animalRepository.save(
                AnimalDTO.dtoMapToExistsAnimal(animalDTO, animal));

        return AnimalDTO.animalMapToDto(updatedAnimal);
    }

    public void delete(Long animalId, String username) {
        final Animal animal = animalRepository.findById(animalId)
                .orElseThrow(AnimalNotFoundException::new);

        if (!userService.checkUserAnimal(animal.getUsername(), username)) {
            throw new UserUnauthorizedException();
        }

        animalRepository.delete(animal);
    }

    public Set<AnimalDTO> findAllAnimalsByUsername(String usernameByToken) {
        return animalRepository.findAllByUsername(usernameByToken)
                .orElseThrow(AnimalNotFoundException::new)
                .stream().map(AnimalDTO::animalMapToDto)
                .collect(Collectors.toSet());

    }
}
