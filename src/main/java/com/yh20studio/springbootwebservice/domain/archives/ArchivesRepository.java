package com.yh20studio.springbootwebservice.domain.archives;
import com.yh20studio.springbootwebservice.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ArchivesRepository extends JpaRepository<Archives, Long> {
    Optional<Archives> findById(Long id);

    @Query("SELECT p " +
            "FROM Archives p " +
            "WHERE p.member.id = :member " +
            "ORDER BY p.id DESC")
    Stream<Archives> findAllByMemberDesc(@Param(value = "member") Long member);

    @Query("SELECT p " +
            "FROM Archives p " +
            "ORDER BY p.id DESC")
    Stream<Archives> findAllDesc();
}
