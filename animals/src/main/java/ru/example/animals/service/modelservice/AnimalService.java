package ru.example.animals.service.modelservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.entity.Animal;
import ru.example.animals.exception.custom_exception.AnimalNotFoundException;
import ru.example.animals.exception.custom_exception.UserUnauthorizedException;
import ru.example.animals.repo.AnimalRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnimalService {
    private final AnimalRepository animalRepository;
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
        final Animal animal = animalRepository.save(
                AnimalDTO.dtoMapToAnimal(animalDTO));
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
