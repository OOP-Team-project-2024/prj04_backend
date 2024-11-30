package com.oop.puangJumJum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="fortune")
@Getter
@Setter
public class Fortune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime date;

    private int totalScore;

    @Column(columnDefinition = "TEXT")
    private String totalFortune;

    @ManyToOne
    @JoinColumn(name = "money_id")
    private Money money;

    @ManyToOne
    @JoinColumn(name = "love_id")
    private Love love;

    @ManyToOne
    @JoinColumn(name = "health_id")
    private Health health;

    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;


}
