package com.oop.puangJumJum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Love extends FortuneDetail{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}