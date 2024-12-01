package com.oop.puangJumJum.repository;

import com.oop.puangJumJum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByStudentNum(String studentNum);
}
