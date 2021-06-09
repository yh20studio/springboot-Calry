package com.yh20studio.springbootwebservice.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByRole(String role);
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
}
