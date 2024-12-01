package com.oop.puangJumJum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRankInfoDTO {
    private String name;
    private int totalScore;
    private int rank;
}