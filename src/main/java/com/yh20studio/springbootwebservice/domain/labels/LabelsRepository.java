package com.yh20studio.springbootwebservice.domain.labels;

import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

public interface LabelsRepository extends JpaRepository<Labels, Long> {

    Optional<Labels> findById(Long id);

    @Query("SELECT p " +
            "FROM Labels p " +
            "WHERE p.member.id = :member " +
            "ORDER BY p.sequence ASC ")
    Stream<Labels> findLabelsByMemberOrderBySequence(@Param(value = "member") Long member);

}