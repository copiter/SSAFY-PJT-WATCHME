package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteAllByToken(String name);
    RefreshToken findByEmail(String email);
}
