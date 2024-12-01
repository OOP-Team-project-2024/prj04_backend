package com.oop.puangJumJum.service;

import com.oop.puangJumJum.dto.request.FortuneRankRequestDTO;
import com.oop.puangJumJum.dto.response.FortuneDetailDto;
import com.oop.puangJumJum.dto.response.FortuneResponseDto;
import com.oop.puangJumJum.repository.FortuneRepository;
import com.oop.puangJumJum.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.oop.puangJumJum.util.FortuneCalculator.calculateFortuneScore;
import com.oop.puangJumJum.dto.response.FortuneRankResponseDTO;
import com.oop.puangJumJum.dto.response.PlaceChoiceResponseDTO;
import com.oop.puangJumJum.dto.response.StudentMenuResponseDTO;
import com.oop.puangJumJum.dto.response.UserRankInfoDTO;
import com.oop.puangJumJum.entity.*;
import com.oop.puangJumJum.exception.InternalServerException;
import com.oop.puangJumJum.exception.UserNotFoundException;
import com.oop.puangJumJum.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class FortuneService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FortuneRepository fortuneRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PlaceRepository placeRepository;
    // 운세 항목에 대한 이모지 유니코드 매핑
    private static final int MONEY_EMOJI = 0x1F4B0;
    private static final int HEALTH_EMOJI = 0x1F3CB;
    private static final int STUDY_EMOJI = 0x1F4DA;
    private static final int LOVE_EMOJI = 0x1F495;


    public FortuneResponseDto generateAndGetFortune(String studentNumber) {
        // 1. 날짜 설정
        LocalDateTime now = LocalDateTime.now();
        String dateString = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 2. 운세 항목 점수 계산
        List<FortuneDetailDto> fortunes = Arrays.asList(
                FortuneDetailDto.builder().type("재물운").score(calculateFortuneScore(dateString, studentNumber, MONEY_EMOJI)).detail("").build(),
                FortuneDetailDto.builder().type("건강운").score(calculateFortuneScore(dateString, studentNumber, HEALTH_EMOJI)).detail("").build(),
                FortuneDetailDto.builder().type("학업운").score(calculateFortuneScore(dateString, studentNumber, STUDY_EMOJI)).detail("").build(),
                FortuneDetailDto.builder().type("애정운").score(calculateFortuneScore(dateString, studentNumber, LOVE_EMOJI)).detail("").build()
        );

        // 3. 총 점수 및 최고의 운세 항목 계산
        int totalScore = fortunes.stream().mapToInt(FortuneDetailDto::getScore).sum() / fortunes.size();
        FortuneDetailDto bestFortune = fortunes.stream().max((f1, f2) -> Integer.compare(f1.getScore(), f2.getScore())).orElse(null);

        // 4. 행운의 장소와 메뉴
        String luckyPlace = getLuckyPlace(dateString, studentNumber);
        String luckyMenu = getLuckyMenu(dateString, studentNumber);

        // 6. 응답 DTO 구성
        return FortuneResponseDto.builder()
                .date(now)
                .totalScore(totalScore)
                .totalFortune("")
                .bestFortune(bestFortune.getType())
                .luckyPlace(luckyPlace)
                .luckyMenu(luckyMenu)
                .fortunes(fortunes)
                .build();
    }


    private String getLuckyPlace(String dateString, String studentNumber) {
        return "";
    }

    private String getLuckyMenu(String dateString, String studentNumber) {
        return "";
    }

    public PlaceChoiceResponseDTO getStudentPlaceAndOthers(FortuneRankRequestDTO requestDTO) {
        String studentNum=requestDTO.getStudentNum();
        User user = userRepository.findByStudentNum(studentNum)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        Place studentPlace = placeRepository.findByUserAndDateAfter(user, today)
                .orElseThrow(() -> new RuntimeException("Place not found for today"));

        PlaceChoice placeChoice = studentPlace.getPlaceChoice();

        List<String> otherUsers = placeRepository.findByPlaceChoice(placeChoice)
                .stream()
                .filter(place -> !place.getUser().equals(user)) // 본인 제외
                .map(place -> place.getUser().getName())
                .collect(Collectors.toList());

        return new PlaceChoiceResponseDTO(user.getName(), placeChoice.getPlace(), placeChoice.getDetail(), otherUsers);
    }

    public FortuneRankResponseDTO getFortuneRank(FortuneRankRequestDTO requestDTO) {
        try {
            User user = userRepository.findByStudentNum(requestDTO.getStudentNum())
                    .orElseThrow(() -> new UserNotFoundException(requestDTO.getStudentNum()));

            List<Fortune> fortunes = fortuneRepository.findAllSortedByTotalScore();

            List<UserRankInfoDTO> rankList = fortunes.stream()
                    .map(fortune -> new UserRankInfoDTO(
                            fortune.getUser().getName(),
                            fortune.getTotalScore(),
                            0
                    ))
                    .sorted((u1, u2) -> Integer.compare(u2.getTotalScore(), u1.getTotalScore())) // 점수 내림차순 정렬
                    .collect(Collectors.toList());

            UserRankInfoDTO myInfo = rankList.stream()
                    .filter(userInfo -> userInfo.getName().equals(user.getName()))
                    .findFirst()
                    .orElseThrow(() -> new UserNotFoundException(requestDTO.getStudentNum()));

            for (int i = 0; i < rankList.size(); i++) {
                rankList.get(i).setRank(i + 1);
            }

            return new FortuneRankResponseDTO(myInfo, rankList);

        } catch (Exception ex) {
            throw new InternalServerException("Failed to process fortune rank");
        }
    }

    public StudentMenuResponseDTO getStudentMenuAndOthers(FortuneRankRequestDTO requestDTO) {
        String studentNum=requestDTO.getStudentNum();

        User user = userRepository.findByStudentNum(studentNum)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        LocalDate today = LocalDate.now();
        Menu menu = menuRepository.findByUserAndDate(user, today)
                .orElseThrow(() -> new IllegalArgumentException("오늘 메뉴가 없습니다"));

        MenuChoice menuChoice = menu.getMenuChoice();

        List<String> otherUsers = menuRepository.findByMenuChoice(menuChoice).stream()
                .filter(m -> !m.getUser().equals(user))
                .map(m -> m.getUser().getName())
                .collect(Collectors.toList());

        return new StudentMenuResponseDTO(user.getName(),menuChoice.getRestaurant(), menuChoice.getMenu(), otherUsers);
    }


}
