package ru.example.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.auth.entity.AccessToken;

import java.util.Optional;

@Repository
public interface AccessTokenRepository  extends JpaRepository<AccessToken, Long> {

    @Override
    Optional<AccessToken> findById(Long id);

    Optional<AccessToken> findByAccessTokenData(String accessTokenData);

    AccessToken findByUserId(Long id);

    void deleteByAccessTokenData(String accessTokenData);
}