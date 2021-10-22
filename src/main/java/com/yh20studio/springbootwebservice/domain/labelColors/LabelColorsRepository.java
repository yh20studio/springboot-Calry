package com.yh20studio.springbootwebservice.domain.labelColors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface LabelColorsRepository extends JpaRepository<LabelColors, Long> {

    Optional<LabelColors> findById(Long id);

    @Query("SELECT p " +
            "FROM LabelColors p " +
            "WHERE p.id <> 1 " +
            "ORDER BY p.id ASC ")
    Stream<LabelColors> findAllWithoutHoliday();

}