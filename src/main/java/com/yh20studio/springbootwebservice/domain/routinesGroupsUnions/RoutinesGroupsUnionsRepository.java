package com.yh20studio.springbootwebservice.domain.routinesGroupsUnions;

import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;

public interface RoutinesGroupsUnionsRepository extends JpaRepository<RoutinesGroupsUnions, Long> {

    Optional<RoutinesGroupsUnions> findById(Long id);

    @Query("SELECT p " +
        "FROM RoutinesGroupsUnions p " +
        "WHERE p.member.id = :member " +
        "ORDER BY p.id ASC ")
    Stream<RoutinesGroupsUnions> findAllByMember(@Param(value = "member") Long member);
}