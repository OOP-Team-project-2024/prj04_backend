package com.oop.puangJumJum.repository;

import com.oop.puangJumJum.entity.Fortune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FortuneRepository extends JpaRepository<Fortune, Long> {

}
