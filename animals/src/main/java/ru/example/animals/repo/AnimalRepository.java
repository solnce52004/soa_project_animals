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

    @Query(value = "INSERT INTO Animals\n" +
            "(username, animal_type_id, animal_name, gender, birthdate)\n" +
            "VALUES (:username, :type, :animal_name, CAST(:gender AS GENDER), :birthdate)" +
            "RETURNING id",
            nativeQuery = true)
    @Transactional
    Long saveByParams(
            @Param("username") String username,
            @Param("type") Long type,
            @Param("animal_name") String animalName,
            @Param("gender") Character gender,
            @Param("birthdate") Date birthdate
    );

    @Modifying
    @Query(value = "UPDATE Animals SET\n" +
            "animal_type_id = :type, " +
            "animal_name = :animal_name, " +
            "gender = CAST(:gender AS GENDER), " +
            "birthdate = :birthdate\n" +
            "WHERE id = :id",
            nativeQuery = true)
    @Transactional
    void updateByIdByParams(
            @Param("id") Long id,
            @Param("type") Long type,
            @Param("animal_name") String animalName,
            @Param("gender") Character gender,
            @Param("birthdate") Date birthdate
    );

    Optional<Set<Animal>> findAllByUsername(String username);

    Animal findByAnimalNameAndUsername(String animalName, String username);

    Optional<Animal> findByIdAndUsername(Long animalId, String username);

    Animal findByAnimalName(String animalName);
}
