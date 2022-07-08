package ru.example.auth.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.auth.entity.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Override
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByRefreshTokenData(String refreshTokenData);

    RefreshToken findByUserId(Long id);

    void deleteByRefreshTokenData(String refreshTokenData);
}
