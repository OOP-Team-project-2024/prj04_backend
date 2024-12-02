package com.oop.puangJumJum.repository;

import com.oop.puangJumJum.entity.Fortune;
import com.oop.puangJumJum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FortuneRepository extends JpaRepository<Fortune, Long> {
    @Query("SELECT f FROM Fortune f ORDER BY f.totalScore DESC")
    List<Fortune> findAllSortedByTotalScore();

    Optional<Fortune> findByUserAndDate(User user, LocalDate date);
}
