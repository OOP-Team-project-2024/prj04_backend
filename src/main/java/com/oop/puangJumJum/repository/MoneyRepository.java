package com.oop.puangJumJum.repository;

import com.oop.puangJumJum.entity.Money;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyRepository extends JpaRepository<Money, Long>{
}
