package ru.example.animals.service.modelservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.dto.AnimalTypeDTO;
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

        if (!userService.isMismatchUserAnimal(animalUsername, requestUsername)) {
            throw new UserUnauthorizedException();
        }

        return AnimalDTO.animalMapToDto(animal);
    }

    @Transactional
    public AnimalDTO create(AnimalDTO animalDTO) {
        final Animal toSaveAnimal = AnimalDTO.dtoMapToAnimal(animalDTO);
        final String title = AnimalTypeDTO
                .dtoMapToAnimalType(animalDTO.getAnimalType())
                .getTitle();

        final Animal existingAnimal = animalRepository.findByAnimalNameAndUsername(
                toSaveAnimal.getAnimalName(),
                toSaveAnimal.getUsername());

        //check unique
        if (existingAnimal != null) {
            throw new AnimalNotUniqueException();
        }

        //animal types already exists in db and immutable
        final AnimalType type = animalTypeRepository.findByTitle(title)
                .orElseThrow(AnimalTypeNotFoundException::new);

        final Animal animal = saveByParams(
                toSaveAnimal.setAnimalType(type));

        return AnimalDTO.animalMapToDto(animal);
    }

    public Animal saveByParams(Animal animal) {
        animalRepository.saveByParams(
                animal.getUsername(),
                animal.getAnimalType().getId(),
                animal.getAnimalName(),
                animal.getGender().getName(),
                animal.getBirthdate()
        );
        return animal;
    }

    public AnimalDTO updateById(Long animalId, AnimalDTO animalDTO) {
        final Animal existingAnimal = animalRepository.findByIdAndUsername(animalId, animalDTO.getUsername())
                .orElseThrow(AnimalNotFoundException::new);

        return updateFromDto(existingAnimal, animalDTO);
    }

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

        final Animal mapped = AnimalDTO.dtoMapToAnimal(animalDTO, animal)
                .setAnimalType(type);

        final Animal updated = animalRepository.save(mapped);
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
}
