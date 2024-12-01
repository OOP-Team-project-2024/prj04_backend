package com.oop.puangJumJum.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public abstract class FortuneDetail {
    private int score;

    @Column(columnDefinition = "TEXT")
    private String content;
}
