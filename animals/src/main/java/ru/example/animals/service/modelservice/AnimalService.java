package ru.example.animals.service.modelservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.entity.Animal;
import ru.example.animals.entity.AnimalType;
import ru.example.animals.exception.custom_exception.AnimalNotFoundException;
import ru.example.animals.exception.custom_exception.AnimalNotUniqueException;
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

        if (userService.isMismatchUserAnimal(animalUsername, requestUsername)) {
            throw new UserUnauthorizedException();
        }

        return AnimalDTO.animalMapToDto(animal);
    }

    @Transactional
    public AnimalDTO create(AnimalDTO animalDTO) {
        final Animal existingAnimal = animalRepository.findByAnimalName(
                animalDTO.getAnimalName());
        //check unique
        if (existingAnimal != null) {
            throw new AnimalNotUniqueException();
        }

        final String title = animalDTO.getAnimalType().getTitle();
        if (title == null) {
            throw new AnimalTypeNotFoundException();
        }
        //animal types already exists in db and immutable
        final AnimalType type = animalTypeRepository.findByTitle(title)
                .orElseThrow(AnimalTypeNotFoundException::new);

        final Animal toSaveAnimal = AnimalDTO.dtoMapToNewAnimal(animalDTO);
        final Animal animal = saveByParams(toSaveAnimal.setAnimalType(type));

        return AnimalDTO.animalMapToDto(animal);
    }

    //patch
    public AnimalDTO updateById(Long animalId, AnimalDTO animalDTO) {
        final Animal existingAnimal = animalRepository.findByIdAndUsername(animalId, animalDTO.getUsername())
                .orElseThrow(AnimalNotFoundException::new);

        if (animalRepository.findByAnimalName(existingAnimal.getAnimalName()) != null) {
            throw new AnimalNotUniqueException();
        }

        return updateFromDto(existingAnimal, animalDTO);
    }

    //put
    public AnimalDTO updateByAnimalName(String animalName, AnimalDTO animalDTO) {
        final Animal existingAnimal = animalRepository.findByAnimalNameAndUsername(
                animalName,
                animalDTO.getUsername());

        if (existingAnimal == null) {
            throw new AnimalNotFoundException();
        }

        return updateFromDto(existingAnimal, animalDTO);
    }

    private AnimalDTO updateFromDto(Animal animal, AnimalDTO animalDTO) {
        final String title = animalDTO.getAnimalType().getTitle();
        if (title == null) {
            throw new AnimalTypeNotFoundException();
        }

        final AnimalType type = animalTypeRepository.findByTitle(title)
                .orElseThrow(AnimalTypeNotFoundException::new);

        final Animal mapped = AnimalDTO.dtoMapToExistsAnimal(animalDTO, animal)
                .setAnimalType(type);

        final Animal updated = updateByParams(mapped);
        return AnimalDTO.animalMapToDto(updated);
    }

    public void delete(Long animalId, String username) {
        final Animal animal = animalRepository.findById(animalId)
                .orElseThrow(AnimalNotFoundException::new);

        if (userService.isMismatchUserAnimal(animal.getUsername(), username)) {
            throw new UserUnauthorizedException();
        }

        animalRepository.delete(animal);
    }

    public Set<AnimalDTO> findAllAnimalsByUsername(String username) {
        return animalRepository.findAllByUsername(username)
                .orElseThrow(AnimalNotFoundException::new)
                .stream()
                .map(AnimalDTO::animalMapToDto)
                .collect(Collectors.toSet());
    }

    private Animal saveByParams(Animal animal) {
        final Long id = animalRepository.saveByParams(
                animal.getUsername(),
                animal.getAnimalType().getId(),
                animal.getAnimalName(),
                animal.getGender().getName(),
                animal.getBirthdate()
        );
        return animal.setId(id);
    }

    private Animal updateByParams(Animal animal) {
        animalRepository.updateByIdByParams(
                animal.getId(),
                animal.getAnimalType().getId(),
                animal.getAnimalName(),
                animal.getGender().getName(),
                animal.getBirthdate()
        );
        return animal;
    }
}
