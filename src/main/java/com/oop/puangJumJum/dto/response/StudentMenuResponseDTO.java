package com.oop.puangJumJum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StudentMenuResponseDTO {
    private String studentName;
    private String restaurant;
    private String menu;
    private List<String> otherUsers;
}