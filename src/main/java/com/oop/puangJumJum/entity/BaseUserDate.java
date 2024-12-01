package com.oop.puangJumJum.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class BaseUserDate {

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private LocalDateTime date;
}
