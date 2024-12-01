package com.oop.puangJumJum.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FortuneRankResponseDTO {
    private UserRankInfoDTO myInfo;
    private List<UserRankInfoDTO> rankList;
}