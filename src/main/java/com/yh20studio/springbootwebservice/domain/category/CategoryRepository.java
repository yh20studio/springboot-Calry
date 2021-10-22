package com.yh20studio.springbootwebservice.domain.category;

import com.yh20studio.springbootwebservice.domain.recommendRoutines.RecommendRoutines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);

}