package ru.example.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.example.auth.entity.AccessToken;

import java.util.Optional;

@Repository
public interface AccessTokenRepository  extends JpaRepository<AccessToken, Long> {

    @Override
    Optional<AccessToken> findById(Long id);

    Optional<AccessToken> findByToken(String token);

    AccessToken findByUserId(Long id);

    @Transactional
    @Modifying
    @Query("delete from AccessToken a where a.token=:token")
    void deleteByToken(@Param("token") String token);
}