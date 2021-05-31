package com.yh20studio.springbootwebservice.domain.user;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByRole(String role);
    Optional<User> findByEmail(String email);
}
