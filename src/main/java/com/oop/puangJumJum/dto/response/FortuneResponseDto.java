package com.oop.puangJumJum.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class FortuneResponseDto {
    private LocalDateTime date;               // 운세 생성 날짜
    private int totalScore;                   // 총 점수
    private String totalFortune;              // 총 운세 설명
    private String bestFortune;               // 최고의 운세 항목
    private String luckyPlace;                // 행운의 장소명
    private String luckyMenu;                 // 행운의 메뉴명
    private List<FortuneDetailDto> fortunes; // 개별 운세 상세 정보 리스트
}