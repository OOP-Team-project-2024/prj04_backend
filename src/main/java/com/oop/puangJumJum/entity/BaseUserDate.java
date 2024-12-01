package com.oop.puangJumJum.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class BaseUserDate {

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private LocalDate date;
}
