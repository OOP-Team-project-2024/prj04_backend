package com.oop.puangJumJum.service;

import com.oop.puangJumJum.dto.response.FortuneDetailDto;
import com.oop.puangJumJum.dto.response.FortuneResponseDto;
import com.oop.puangJumJum.repository.FortuneRepository;
import com.oop.puangJumJum.repository.MenuChoiceRepository;
import com.oop.puangJumJum.repository.PlaceChoiceRepository;
import com.oop.puangJumJum.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.oop.puangJumJum.util.FortuneCalculator.calculateFortuneScore;

@Service
public class FortuneService {

    private final UserRepository userRepository;
    private final FortuneRepository fortuneRepository;
    private final MenuChoiceRepository menuChoiceRepository;
    private final PlaceChoiceRepository placeChoiceRepository;

    // 운세 항목에 대한 이모지 유니코드 매핑
    private static final int MONEY_EMOJI = 0x1F4B0;
    private static final int HEALTH_EMOJI = 0x1F3CB;
    private static final int STUDY_EMOJI = 0x1F4DA;
    private static final int LOVE_EMOJI = 0x1F495;

    public FortuneService(UserRepository userRepository, FortuneRepository fortuneRepository,
                          MenuChoiceRepository menuChoiceRepository, PlaceChoiceRepository placeChoiceRepository) {
        this.userRepository = userRepository;
        this.fortuneRepository = fortuneRepository;
        this.menuChoiceRepository = menuChoiceRepository;
        this.placeChoiceRepository = placeChoiceRepository;
    }

    public FortuneResponseDto generateAndGetFortune(String studentNumber) {
        // 1. 날짜 설정
        LocalDateTime now = LocalDateTime.now();
        String dateString = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 2. 운세 항목 점수 계산
        List<FortuneDetailDto> fortunes = Arrays.asList(
                FortuneDetailDto.builder().type("재물운").score(calculateFortuneScore(dateString, studentNumber, 0x1F4B0)).detail("").build(),
                FortuneDetailDto.builder().type("건강운").score(calculateFortuneScore(dateString, studentNumber, 0x1F3CB)).detail("").build(),
                FortuneDetailDto.builder().type("학업운").score(calculateFortuneScore(dateString, studentNumber, 0x1F4DA)).detail("").build(),
                FortuneDetailDto.builder().type("애정운").score(calculateFortuneScore(dateString, studentNumber, 0x1F495)).detail("").build()
        );

        // 3. 총 점수 및 최고의 운세 항목 계산
        int totalScore = fortunes.stream().mapToInt(FortuneDetailDto::getScore).sum() / fortunes.size();
        FortuneDetailDto bestFortune = fortunes.stream().max((f1, f2) -> Integer.compare(f1.getScore(), f2.getScore())).orElse(null);

        // 4. 행운의 장소와 메뉴
        String luckyPlace = getLuckyPlace(dateString, studentNumber);
        String luckyMenu = getLuckyMenu(dateString, studentNumber);

        // 5. 운세 등수
        int fortuneRank = calculateFortuneRank(totalScore);

        // 6. 응답 DTO 구성
        return FortuneResponseDto.builder()
                .date(now)
                .totalScore(totalScore)
                .totalFortune("")
                .bestFortune(bestFortune.getType())
                .luckyPlace(luckyPlace)
                .luckyMenu(luckyMenu)
                .fortuneRank(fortuneRank)
                .fortunes(fortunes)
                .build();
    }

    private String getBestFortune(int moneyScore, int healthScore, int studyScore, int loveScore) {
        int maxScore = Math.max(Math.max(moneyScore, healthScore), Math.max(studyScore, loveScore));
        if (maxScore == moneyScore) return "재물운";
        else if (maxScore == healthScore) return "건강운";
        else if (maxScore == studyScore) return "학업운";
        else return "애정운";
    }

    private String getLuckyPlace(String dateString, String studentNumber) {
        return "";
    }

    private String getLuckyMenu(String dateString, String studentNumber) {
        return "";
    }

    private int calculateFortuneRank(int totalScore) {
        return 0;
    }
}
