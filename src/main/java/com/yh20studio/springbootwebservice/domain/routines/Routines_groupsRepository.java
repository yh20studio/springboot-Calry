package com.yh20studio.springbootwebservice.domain.routines;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;


public interface Routines_groupsRepository extends JpaRepository<Routines_groups, Long> {
    Optional<Routines_groups> findById(Long id);

    @Query("SELECT p " +
            "FROM Routines_groups p " +
            "WHERE p.member.id = :member " +
            "ORDER BY p.id DESC")
    Stream<Routines_groups> findAllByMemberDesc(@Param(value = "member") Long member);

    @Query("SELECT p " +
            "FROM Routines_groups p " +
            "ORDER BY p.id DESC")
    Stream<Routines_groups> findAllDesc();
}
