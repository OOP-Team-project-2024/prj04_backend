package com.oop.puangJumJum.service;

import com.oop.puangJumJum.dto.response.FortuneRankResponseDTO;
import com.oop.puangJumJum.dto.response.UserInfoDTO;
import com.oop.puangJumJum.entity.Fortune;
import com.oop.puangJumJum.entity.User;
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

    public FortuneRankResponseDTO getFortuneRank(String studentNum) {
        User user = userRepository.findByStudentNum(studentNum)
                .orElseThrow(() -> new IllegalArgumentException("User not found with studentNum: " + studentNum));

        List<Fortune> fortunes = fortuneRepository.findAll();

        List<UserInfoDTO> rankList = fortunes.stream()
                .map(fortune -> {
                    UserInfoDTO userInfo = new UserInfoDTO(
                            fortune.getUser().getName(),
                            fortune.getTotalScore(),
                            0
                    );
                    return userInfo;
                })
                .sorted((u1, u2) -> Integer.compare(u2.getTotalScore(), u1.getTotalScore()))
                .collect(Collectors.toList());

        UserInfoDTO myInfo = rankList.stream()
                .filter(userInfo -> userInfo.getName().equals(user.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found in rank list"));

        for (int i = 0; i < rankList.size(); i++) {
            rankList.get(i).setRank(i + 1);
        }

        return new FortuneRankResponseDTO(myInfo, rankList);
    }
}