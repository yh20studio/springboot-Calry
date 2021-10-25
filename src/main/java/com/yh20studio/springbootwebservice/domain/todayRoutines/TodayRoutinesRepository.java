package com.yh20studio.springbootwebservice.domain.todayRoutines;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;

public interface TodayRoutinesRepository extends JpaRepository<TodayRoutines, Long> {
    Optional<TodayRoutines> findById(Long id);

    @Query("SELECT p " +
            "FROM TodayRoutines p " +
            "WHERE p.routines.member.id = :member " +
            "ORDER BY p.id ASC")
    Stream<TodayRoutines> findAllByMemberASC(@Param(value = "member") Long member);

}