package com.oop.puangJumJum.controller;


import com.oop.puangJumJum.dto.request.FortuneRankRequestDTO;
import com.oop.puangJumJum.dto.request.FortuneRequestDTO;
import com.oop.puangJumJum.dto.response.FortuneRankResponseDTO;
import com.oop.puangJumJum.dto.response.FortuneResponseDto;
import com.oop.puangJumJum.dto.response.PlaceChoiceResponseDTO;
import com.oop.puangJumJum.dto.response.StudentMenuResponseDTO;
import com.oop.puangJumJum.service.FortuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fortune")
public class FortuneController {

    @Autowired
    private FortuneService fortuneService;

    @GetMapping("/rank")
    public ResponseEntity<FortuneRankResponseDTO> getFortuneRank(@RequestParam String studentNum) {
        FortuneRankResponseDTO response = fortuneService.getFortuneRank(studentNum);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/menu")
    public ResponseEntity<StudentMenuResponseDTO> getStudentMenuAndOthers(@RequestParam String studentNum) {
        StudentMenuResponseDTO responseDTO = fortuneService.getStudentMenuAndOthers(studentNum);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/place")
    public ResponseEntity<PlaceChoiceResponseDTO> getStudentPlaceAndOthers(@RequestParam String studentNum) {
        PlaceChoiceResponseDTO responseDTO = fortuneService.getStudentPlaceAndOthers(studentNum);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<FortuneResponseDto> getStudentFortune(@RequestBody FortuneRequestDTO requestDTO){
        FortuneResponseDto responseDto = fortuneService.generateAndGetFortune(requestDTO);
        return ResponseEntity.ok(responseDto);
    }
}
