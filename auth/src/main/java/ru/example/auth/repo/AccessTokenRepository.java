package ru.example.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.example.auth.entity.AccessToken;

import java.util.Optional;

@Repository
public interface AccessTokenRepository  extends JpaRepository<AccessToken, Long> {

    @Override
    Optional<AccessToken> findById(Long id);

    Optional<AccessToken> findByAccessTokenData(String accessTokenData);

    @Query("select count(sa) from sign_in_logs sa where sa.createdAt > :start and sa.ip = :ip")
    AccessToken updateOrCreate(AccessToken accessToken);

    AccessToken findByUserId(Long id);
}