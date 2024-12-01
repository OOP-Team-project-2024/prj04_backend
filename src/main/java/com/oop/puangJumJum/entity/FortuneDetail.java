package com.oop.puangJumJum.entity;

import jakarta.persistence.*;
import lombok.*;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public abstract class FortuneDetail {
    private int score;

    @Column(columnDefinition = "TEXT")
    private String content;
}
