package com.oop.puangJumJum.service;

import com.oop.puangJumJum.dto.request.FortuneRankRequestDTO;
import com.oop.puangJumJum.dto.response.FortuneRankResponseDTO;
import com.oop.puangJumJum.dto.response.UserInfoDTO;
import com.oop.puangJumJum.entity.Fortune;
import com.oop.puangJumJum.entity.User;
import com.oop.puangJumJum.exception.InternalServerException;
import com.oop.puangJumJum.exception.UserNotFoundException;
import com.oop.puangJumJum.repository.FortuneRepository;
import com.oop.puangJumJum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FortuneService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FortuneRepository fortuneRepository;

    public FortuneRankResponseDTO getFortuneRank(FortuneRankRequestDTO requestDTO) {
        try {
            User user = userRepository.findByStudentNum(requestDTO.getStudentNum())
                    .orElseThrow(() -> new UserNotFoundException(requestDTO.getStudentNum()));

            List<Fortune> fortunes = fortuneRepository.findAllSortedByTotalScore();

            List<UserInfoDTO> rankList = fortunes.stream()
                    .map(fortune -> new UserInfoDTO(
                            fortune.getUser().getName(),
                            fortune.getTotalScore(),
                            0
                    ))
                    .sorted((u1, u2) -> Integer.compare(u2.getTotalScore(), u1.getTotalScore())) // 점수 내림차순 정렬
                    .collect(Collectors.toList());

            UserInfoDTO myInfo = rankList.stream()
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
}