package ru.example.animals.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.animals.entity.AnimalType;

import java.util.Optional;

@Repository
public interface AnimalTypeRepository extends JpaRepository<AnimalType, Long> {

    @EntityGraph(
            value = "animal-type",
            type = EntityGraph.EntityGraphType.LOAD)
    @Override
    Optional<AnimalType> findById(final Long aLong);
}
