package ru.example.animals.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.example.animals.entity.Animal;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    @Modifying
    @Query(value = "INSERT INTO Animals\n" +
            "(username, animal_type_id, animal_name, gender, birthdate)\n" +
            "VALUES (:username, :type, :animal_name, CAST(:gender AS gender), :birthdate)",
            nativeQuery = true)
    @Transactional
    void saveByParams(
            @Param("username") String username,
            @Param("type") Long type,
            @Param("animal_name") String animalName,
            @Param("gender") Character gender,
            @Param("birthdate") Date birthdate
    );

    Optional<Set<Animal>> findAllByUsername(String username);

    Animal findByUsernameAndAnimalName(String username, String animalName);
}
