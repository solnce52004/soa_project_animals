package ru.example.animals.service.modelservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.animals.entity.AnimalType;
import ru.example.animals.exception.custom_exception.animal.AnimalTypeNotFoundException;
import ru.example.animals.repo.AnimalTypeRepository;

@Service
@AllArgsConstructor
public class AnimalTypeService {
    private final AnimalTypeRepository animalTypeRepository;

    public AnimalType findByTitle(String title){
        return  animalTypeRepository.findByTitle(title)
                .orElseThrow(AnimalTypeNotFoundException::new);
    }
}