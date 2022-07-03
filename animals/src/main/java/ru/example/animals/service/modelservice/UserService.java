package ru.example.animals.service.modelservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.animals.entity.User;
import ru.example.animals.repo.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(long userId){
        return userRepository
                .findById(userId)
                .orElseGet(User::new);
    }

    public boolean checkAuthAnimalUser(Long animalUserId, Long requestUserId) {
        return animalUserId != null &&
                requestUserId != null &&
                !animalUserId.equals(requestUserId);
    }
}
