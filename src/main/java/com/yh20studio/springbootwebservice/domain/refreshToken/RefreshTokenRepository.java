package com.yh20studio.springbootwebservice.domain.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByKey(String key);

    @Transactional
    @Modifying
    @Query("delete from RefreshToken u where u.key = :key")
    void deleteByKey(@Param("key") String key);
}
