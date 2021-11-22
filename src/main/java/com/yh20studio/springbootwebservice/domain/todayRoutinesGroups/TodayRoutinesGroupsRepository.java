package com.yh20studio.springbootwebservice.domain.todayRoutinesGroups;

import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;


public interface TodayRoutinesGroupsRepository extends JpaRepository<TodayRoutinesGroups, Long> {

    Optional<TodayRoutinesGroups> findById(Long id);

    @Query("SELECT p " +
        "FROM TodayRoutinesGroups p " +
        "WHERE p.member.id = :member and p.date = :date")
    Optional<TodayRoutinesGroups> findByMemberAndDate(@Param(value = "member") Long member,
        @Param(value = "date") LocalDate date);

    @Query("SELECT p " +
        "FROM TodayRoutinesGroups p " +
        "WHERE p.member.id = :member " +
        "ORDER BY p.id ASC ")
    Stream<TodayRoutinesGroups> findAllByMember(@Param(value = "member") Long member);

}