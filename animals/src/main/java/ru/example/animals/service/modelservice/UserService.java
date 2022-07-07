package ru.example.animals.service.modelservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    public boolean checkUserAnimal(String animalUsername, String requestUsername) {
        return animalUsername != null &&
                requestUsername != null &&
                !animalUsername.equals(requestUsername);
    }
}
