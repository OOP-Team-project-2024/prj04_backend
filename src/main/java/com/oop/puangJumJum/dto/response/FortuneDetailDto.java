package com.oop.puangJumJum.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FortuneDetailDto {
    private String type;   // 운세 유형 (예: "재물운", "건강운")
    private int score;     // 점수 (50~100 사이)
    private String detail; // 운세 설명
}