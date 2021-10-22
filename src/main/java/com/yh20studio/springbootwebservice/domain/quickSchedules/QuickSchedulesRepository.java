package com.yh20studio.springbootwebservice.domain.quickSchedules;

import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.stream.Stream;


public interface QuickSchedulesRepository extends JpaRepository<QuickSchedules, Long> {
    Optional<QuickSchedules> findById(Long id);

    @Query("SELECT p " +
            "FROM QuickSchedules p " +
            "WHERE p.member.id = :member " +
            "ORDER BY p.id ASC ")
    Stream<QuickSchedules> findMySchedules(@Param(value = "member") Long member);
}
