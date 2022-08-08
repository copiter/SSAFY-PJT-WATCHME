package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteAllByToken(String name);
    Optional<RefreshToken> findByEmail(String email);
}
