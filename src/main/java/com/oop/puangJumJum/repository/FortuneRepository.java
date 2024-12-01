package com.oop.puangJumJum.repository;

import com.oop.puangJumJum.entity.Fortune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FortuneRepository extends JpaRepository<Fortune, Long> {
    @Query("SELECT f FROM Fortune f ORDER BY f.totalScore DESC")
    List<Fortune> findAllSortedByTotalScore();
}
