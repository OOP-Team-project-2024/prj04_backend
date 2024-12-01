package com.oop.puangJumJum.repository;

import com.oop.puangJumJum.entity.Health;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRepository extends JpaRepository<Health, Long> {
}
