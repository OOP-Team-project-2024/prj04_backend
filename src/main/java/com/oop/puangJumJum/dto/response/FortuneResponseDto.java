package com.oop.puangJumJum.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class FortuneResponseDto {
    private LocalDate date;               // 운세 생성 날짜
    private int totalScore;                   // 총 점수
    private String totalFortune;              // 총 운세 설명
    private List<FortuneDetailDto> fortunes; // 개별 운세 상세 정보 리스트
}