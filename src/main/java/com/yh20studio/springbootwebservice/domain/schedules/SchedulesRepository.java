package com.yh20studio.springbootwebservice.domain.schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

public interface SchedulesRepository extends JpaRepository<Schedules, Long> {
    Optional<Schedules> findById(Long id);

    @Query("SELECT p " +
            "FROM Schedules p " +
            "WHERE p.end_date >= :start_date and p.start_date <= :end_date and p.labels.member.id = :member " +
            "ORDER BY p.id ASC ")
    Stream<Schedules> findMySchedulesByStartDateAndEndDate(@Param(value = "start_date") LocalDateTime start_date, @Param(value = "end_date") LocalDateTime end_date, @Param(value = "member") Long member);

    @Query("SELECT p " +
            "FROM Schedules p " +
            "WHERE p.labels.member.id = :member " +
            "ORDER BY p.id ASC ")
    Stream<Schedules> findMySchedules(@Param(value = "member") Long member);
}
