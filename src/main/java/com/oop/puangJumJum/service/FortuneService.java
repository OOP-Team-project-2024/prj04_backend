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
import java.util.Optional;
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

    @Autowired
    private MoneyRepository moneyRepository;

    @Autowired
    private HealthRepository healthRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private LoveRepository loveRepository;
    // 운세 항목에 대한 이모지 유니코드 매핑
    private static final int MONEY_EMOJI = 0x1F4B0;
    private static final int HEALTH_EMOJI = 0x1F4AA;
    private static final int STUDY_EMOJI = 0x1F4DA;
    private static final int LOVE_EMOJI = 0x1F495;
    @Autowired
    private PlaceChoiceRepository placeChoiceRepository;
    @Autowired
    private MenuChoiceRepository menuChoiceRepository;
    @Autowired
    private OpenAIService openAIService;
    public FortuneResponseDto generateAndGetFortune(FortuneRankRequestDTO requestDTO) {
        String studentNumber = requestDTO.getStudentNum();
        // 1. 날짜 설정
        LocalDate now = LocalDate.now();
        String dateString = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        User user = userRepository.findByStudentNum(studentNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));


        // 기존 운세 확인
        Optional<Fortune> existingFortune = fortuneRepository.findByUserAndDate(user, now);
        if (existingFortune.isPresent()) {
            Fortune fortune = existingFortune.get();

            // 기존 운세가 있을 경우 DTO 반환
            return FortuneResponseDto.builder()
                    .date(fortune.getDate())
                    .totalScore(fortune.getTotalScore())
                    .totalFortune(fortune.getTotalFortune())
                    .fortunes(Arrays.asList(
                            FortuneDetailDto.builder().type("재물운").score(fortune.getMoney().getScore()).detail(fortune.getMoney().getContent()).build(),
                            FortuneDetailDto.builder().type("건강운").score(fortune.getHealth().getScore()).detail(fortune.getHealth().getContent()).build(),
                            FortuneDetailDto.builder().type("학업운").score(fortune.getStudy().getScore()).detail(fortune.getStudy().getContent()).build(),
                            FortuneDetailDto.builder().type("애정운").score(fortune.getLove().getScore()).detail(fortune.getLove().getContent()).build()
                    ))
                    .build();
        }

        // 2. 운세 항목 생성
        List<FortuneDetailDto> fortunes = Arrays.asList(
                createFortuneDetail("재물운", calculateFortuneScore(dateString, studentNumber, MONEY_EMOJI), user.getName()),
                createFortuneDetail("건강운", calculateFortuneScore(dateString, studentNumber, HEALTH_EMOJI), user.getName()),
                createFortuneDetail("학업운", calculateFortuneScore(dateString, studentNumber, STUDY_EMOJI), user.getName()),
                createFortuneDetail("애정운", calculateFortuneScore(dateString, studentNumber, LOVE_EMOJI), user.getName())
        );
        // 3. 총 점수
        int totalScore = fortunes.stream().mapToInt(FortuneDetailDto::getScore).sum() / fortunes.size();
        // 총평
        String totalFortune = openAIService.generateFortuneDescription("총평", totalScore, user.getName());

        // 4. 행운의 장소와 메뉴
        PlaceChoice luckyPlace = getLuckyPlace(dateString, studentNumber);
        Place place = Place.builder()
                .placeChoice(luckyPlace)
                .date(now)
                .user(userRepository.findByStudentNum(studentNumber).orElseThrow(() -> new RuntimeException("User not found")))
                .build();
        placeRepository.save(place);
        MenuChoice luckyMenu = getLuckyMenu(dateString, studentNumber);
        Menu menu = Menu.builder()
                .menuChoice(luckyMenu)
                .date(now)
                .user(userRepository.findByStudentNum(studentNumber).orElseThrow(() -> new RuntimeException("User not found")))
                .build();
        menuRepository.save(menu);

        // 5. 운세 저장
        Money money = Money.builder()
                .score(fortunes.get(0).getScore())
                .content(fortunes.get(0).getDetail())
                .build();
        moneyRepository.save(money);
        Health health = Health.builder()
                .score(fortunes.get(1).getScore())
                .content(fortunes.get(1).getDetail())
                .build();
        healthRepository.save(health);
        Study study = Study.builder()
                .score(fortunes.get(2).getScore())
                .content(fortunes.get(2).getDetail())
                .build();
        studyRepository.save(study);
        Love love = Love.builder()
                .score(fortunes.get(3).getScore())
                .content(fortunes.get(3).getDetail())
                .build();
        loveRepository.save(love);
        Fortune fortune = Fortune.builder()
                .user(user)
                .date(now)
                .totalScore(totalScore)
                .totalFortune("")
                .money(money)
                .health(health)
                .study(study)
                .love(love)
                .build();
        fortuneRepository.save(fortune);
        // 6. 응답 DTO 구성
        return FortuneResponseDto.builder()
                .date(now)
                .totalScore(totalScore)
                .totalFortune(totalFortune)
                .fortunes(fortunes)
                .build();
    }
    private FortuneDetailDto createFortuneDetail(String type, int score, String userName) {
        String description = openAIService.generateFortuneDescription(type, score, userName);
        return FortuneDetailDto.builder()
                .type(type)
                .score(score)
                .detail(description)
                .build();
    }


    private PlaceChoice getLuckyPlace(String dateString, String studentNumber) {
        // 날짜와 학생 번호를 기반으로 "운이 좋은 장소"를 계산
        int hash = (dateString + studentNumber).hashCode(); // 해시값 생성
        List<PlaceChoice> placeChoices = placeChoiceRepository.findAll(); // DB에서 모든 PlaceChoice 조회

        if (placeChoices.isEmpty()) {
            throw new RuntimeException("No places available in the database");
        }

        int index = Math.abs(hash) % placeChoices.size(); // 해시값으로 리스트의 인덱스 결정
        return placeChoices.get(index); // 운이 좋은 PlaceChoice 객체 반환
    }

    private MenuChoice getLuckyMenu(String dateString, String studentNumber) {
        // 날짜와 학생 번호를 기반으로 "운이 좋은 메뉴"를 계산
        int hash = (dateString + studentNumber).hashCode(); // 해시값 생성
        List<MenuChoice> menuChoices = menuChoiceRepository.findAll(); // DB에서 모든 MenuChoice 조회

        if (menuChoices.isEmpty()) {
            throw new RuntimeException("No menus available in the database");
        }

        int index = Math.abs(hash) % menuChoices.size(); // 해시값으로 리스트의 인덱스 결정
        return menuChoices.get(index); // 운이 좋은 MenuChoice 객체 반환
    }

    public PlaceChoiceResponseDTO getStudentPlaceAndOthers(FortuneRankRequestDTO requestDTO) {
        String studentNum=requestDTO.getStudentNum();
        User user = userRepository.findByStudentNum(studentNum)
                .orElseThrow(() -> new RuntimeException("User not found"));

//        LocalDate today = LocalDate.now();
        Place studentPlace = placeRepository.findByUserAndDateAfter(user, LocalDate.now())
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

            List<Fortune> fortunes = fortuneRepository.findAllSortedByTotalScore(LocalDate.now());

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
