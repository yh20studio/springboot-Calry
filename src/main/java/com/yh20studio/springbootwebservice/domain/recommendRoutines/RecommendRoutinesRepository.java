package com.yh20studio.springbootwebservice.domain.recommendRoutines;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendRoutinesRepository extends JpaRepository<RecommendRoutines, Long> {

    Optional<RecommendRoutines> findById(Long id);
}