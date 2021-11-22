package com.yh20studio.springbootwebservice.domain.routinesMemos;

import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;


public interface RoutinesMemosRepository extends JpaRepository<RoutinesMemos, Long> {

    Optional<RoutinesMemos> findById(Long id);
}
