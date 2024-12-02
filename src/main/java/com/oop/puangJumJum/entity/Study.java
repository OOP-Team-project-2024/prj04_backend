package com.oop.puangJumJum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
public class Study extends FortuneDetail{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
