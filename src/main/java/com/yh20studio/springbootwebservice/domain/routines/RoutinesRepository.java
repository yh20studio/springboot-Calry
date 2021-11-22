package com.yh20studio.springbootwebservice.domain.routines;

import com.yh20studio.springbootwebservice.domain.routines.Routines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;


public interface RoutinesRepository extends JpaRepository<Routines, Long> {

    Optional<Routines> findById(Long id);

    @Query("SELECT p " +
        "FROM Routines p " +
        "WHERE p.member.id = :member " +
        "ORDER BY p.id ASC")
    Stream<Routines> findAllByMemberASC(@Param(value = "member") Long member);
}
