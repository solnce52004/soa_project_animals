package ru.example.animals.service.modelservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.dto.request.PatchAnimalTypeRequestDTO;
import ru.example.animals.entity.Animal;
import ru.example.animals.entity.AnimalType;
import ru.example.animals.enums.GenderType;
import ru.example.animals.exception.custom_exception.*;
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
    @Transactional
    public Animal patchAnimalTypeByAnimalId(Long id, PatchAnimalTypeRequestDTO dto) {
        //by id
        final Animal animal = animalRepository.findById(id)
                .orElseThrow(AnimalNotFoundException::new);
        //other user
        if (!dto.getUsername().equals(animal.getUsername())) {
            throw new AnimalNotSupportedByUserException();
        }
        //empty
        if (dto.getAnimalType() == null) {
            throw new AnimalTypeNotFoundException();
        }
        final String titleDto = dto.getAnimalType().getTitle();
        // null/""
        if (titleDto == null || titleDto.equals("")) {
            throw new AnimalTypeNotFoundException();
        }
        //equals
        if (titleDto.equals(animal.getAnimalType().getTitle())) {
            return animal;
        }
        //by titleDto
        final AnimalType typeDb = animalTypeRepository.findByTitle(titleDto)
                .orElseThrow(AnimalTypeNotFoundException::new);

        animalRepository.setFixedAnimalTypeIdWhereId(typeDb, id);
        return animal.setAnimalType(typeDb);
    }


    //put
    public Animal put(Long id, AnimalDTO dto) {
        //by id
        final Animal existingAnimal = animalRepository.findById(id)
                .orElseThrow(AnimalNotFoundException::new);
        //other user
        if (!dto.getUsername().equals(existingAnimal.getUsername())) {
            throw new AnimalNotSupportedByUserException();
        }

        // new animal name
        if (!dto.getAnimalName().equals(existingAnimal.getAnimalName())) {
            //duplicate animal name
            final Animal existingAnimalName = animalRepository.findByAnimalName(dto.getAnimalName());
            if (existingAnimalName != null) {
                throw new DuplicateAnimalNameException();
            }
        }

        //AnimalType
        final String title = dto.getAnimalType().getTitle();
        if (title == null) {
            throw new AnimalTypeNotFoundException();
        }
        final AnimalType type = animalTypeRepository.findByTitle(title)
                .orElseThrow(AnimalTypeNotFoundException::new);

        // mapping all
        final Animal mapped = existingAnimal
                .setUsername(dto.getUsername())
                .setAnimalName(dto.getAnimalName())
                .setAnimalType(type)
                .setGender(GenderType.getOrDefaultGenderName(dto.getGender()))
                .setBirthdate(dto.getBirthdate());

        return updateByParams(mapped);
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
