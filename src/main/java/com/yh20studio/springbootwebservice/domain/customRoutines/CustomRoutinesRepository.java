package com.yh20studio.springbootwebservice.domain.customRoutines;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;

public interface CustomRoutinesRepository extends JpaRepository<CustomRoutines, Long> {
    Optional<CustomRoutines> findById(Long id);

    @Query("SELECT p " +
            "FROM CustomRoutines p " +
            "WHERE p.member.id = :member " +
            "ORDER BY p.id DESC")
    Stream<CustomRoutines> findAllByMemberDesc(@Param(value = "member") Long member);

    @Query("SELECT p " +
            "FROM CustomRoutines p " +
            "ORDER BY p.id DESC")
    Stream<CustomRoutines> findAllDesc();
}