package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
