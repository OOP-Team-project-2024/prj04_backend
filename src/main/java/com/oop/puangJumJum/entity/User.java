package com.oop.puangJumJum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=8, nullable=false, unique=true)
    private String studentNum;

    @Column(length=100, nullable=false)
    private String name;
}
