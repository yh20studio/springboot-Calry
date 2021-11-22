package com.yh20studio.springbootwebservice.domain.routinesGroups;

import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;


public interface RoutinesGroupsRepository extends JpaRepository<RoutinesGroups, Long> {

    Optional<RoutinesGroups> findById(Long id);
}
