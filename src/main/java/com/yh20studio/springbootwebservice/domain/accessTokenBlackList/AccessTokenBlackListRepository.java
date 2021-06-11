package com.yh20studio.springbootwebservice.domain.accessTokenBlackList;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenBlackListRepository extends JpaRepository<AccessTokenBlackList, Long> {

    boolean existsByValue(String value);
}
