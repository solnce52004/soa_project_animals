package ru.example.animals.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.animals.entity.Animal;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Set<Animal>> findAllByUsername(String usernameByToken);
}
