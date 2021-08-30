package com.yh20studio.springbootwebservice.domain.routines;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;


public interface Routines_memosRepository extends JpaRepository<Routines_memos, Long> {
    Optional<Routines_memos> findById(Long id);

    @Query("SELECT p " +
            "FROM Routines_memos p " +
            "WHERE p.routines.id = :routines " +
            "ORDER BY p.id DESC")
    Stream<Routines_memos> findAllByRoutinesDesc(@Param(value = "routines") Long routines);

    @Query("SELECT p " +
            "FROM Routines_memos p " +
            "ORDER BY p.id DESC")
    Stream<Routines_memos> findAllDesc();
}
