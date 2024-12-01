package com.oop.puangJumJum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PlaceChoiceResponseDTO {
    private String studentName;
    private String place;
    private String detail;
    private List<String> otherUsers;
}
