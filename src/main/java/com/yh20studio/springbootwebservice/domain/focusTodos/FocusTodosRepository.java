package com.yh20studio.springbootwebservice.domain.focusTodos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;

public interface FocusTodosRepository extends JpaRepository<FocusTodos, Long> {

    Optional<FocusTodos> findById(Long id);

    @Query("SELECT p " +
            "FROM FocusTodos p " +
            "WHERE p.member.id = :member and p.success = false " +
            "ORDER BY p.id ASC ")
    Stream<FocusTodos> findNotSuccessByMemberId(@Param(value = "member") Long member);
}
