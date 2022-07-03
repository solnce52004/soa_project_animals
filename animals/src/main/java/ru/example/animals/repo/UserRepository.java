package ru.example.animals.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.animals.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {

    @EntityGraph(
            value = "user",
            type = EntityGraph.EntityGraphType.LOAD)
    @Override
    Optional<User> findById(final Long aLong);
}
