package com.oop.puangJumJum.service;

import com.oop.puangJumJum.dto.request.FortuneRankRequestDTO;
import com.oop.puangJumJum.dto.response.FortuneRankResponseDTO;
import com.oop.puangJumJum.dto.response.StudentMenuResponseDTO;
import com.oop.puangJumJum.dto.response.UserRankInfoDTO;
import com.oop.puangJumJum.entity.Fortune;
import com.oop.puangJumJum.entity.Menu;
import com.oop.puangJumJum.entity.MenuChoice;
import com.oop.puangJumJum.entity.User;
import com.oop.puangJumJum.exception.InternalServerException;
import com.oop.puangJumJum.exception.UserNotFoundException;
import com.oop.puangJumJum.repository.FortuneRepository;
import com.oop.puangJumJum.repository.MenuChoiceRepository;
import com.oop.puangJumJum.repository.MenuRepository;
import com.oop.puangJumJum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
    private MenuChoiceRepository menuChoiceRepository;

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

        return new StudentMenuResponseDTO(user.getName(), menuChoice.getMenu(), otherUsers);
    }

}