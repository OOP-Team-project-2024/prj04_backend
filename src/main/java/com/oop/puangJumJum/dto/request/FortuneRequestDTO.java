package com.oop.puangJumJum.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FortuneRequestDTO {
    private String studentNum; // 학생 번호
    private String name;       // 학생 이름
}