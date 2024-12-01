package com.oop.puangJumJum.repository;

import com.oop.puangJumJum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByStudentNum (String studentNum);
    @Query("SELECT COUNT(f) + 1 " +
            "FROM Fortune f " +
            "WHERE f.totalScore > (SELECT ff.totalScore FROM Fortune ff WHERE ff.user.id = :userId)")
    int findRankByUserId(@Param("userId") Long userId);
}
