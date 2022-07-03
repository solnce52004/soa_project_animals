package ru.example.animals.service.modelservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.animals.entity.AnimalType;
import ru.example.animals.repo.AnimalTypeRepository;

@Service
@AllArgsConstructor
public class AnimalTypeService {
    private final AnimalTypeRepository animalRepository;

    public AnimalType findById(long animalTypeId){
        return animalRepository.findById(animalTypeId).orElseGet(AnimalType::new);
    }
}