package com.yh20studio.springbootwebservice.domain.routinesCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoutinesCategoryRepository extends JpaRepository<RoutinesCategory, Long> {
    Optional<RoutinesCategory> findById(Long id);

}