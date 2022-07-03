package ru.example.animals.service.modelservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.animals.entity.Animal;
import ru.example.animals.repo.AnimalRepository;

@Service
@AllArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    public Animal findById(long animalId){
        return animalRepository
                .findById(animalId)
                .orElseGet(Animal::new);
    }
}
