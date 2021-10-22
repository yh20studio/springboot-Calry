package com.yh20studio.springbootwebservice.domain.recommendRoutines;

import com.yh20studio.springbootwebservice.domain.customRoutines.CustomRoutines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;

public interface RecommendRoutinesRepository extends JpaRepository<RecommendRoutines, Long> {
    Optional<RecommendRoutines> findById(Long id);

}