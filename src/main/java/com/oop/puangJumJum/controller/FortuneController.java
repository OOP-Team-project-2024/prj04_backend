package com.oop.puangJumJum.controller;


import com.oop.puangJumJum.dto.request.FortuneRankRequestDTO;
import com.oop.puangJumJum.dto.response.FortuneRankResponseDTO;
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

    @PostMapping("/rank")
    public ResponseEntity<FortuneRankResponseDTO> getFortuneRank(@RequestBody FortuneRankRequestDTO requestDTO) {
        FortuneRankResponseDTO response = fortuneService.getFortuneRank(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/menu")
    public ResponseEntity<StudentMenuResponseDTO> getStudentMenuAndOthers(@RequestBody FortuneRankRequestDTO requestDTO) {
        StudentMenuResponseDTO responseDTO = fortuneService.getStudentMenuAndOthers(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/place")
    public ResponseEntity<PlaceChoiceResponseDTO> getStudentPlaceAndOthers(@RequestBody FortuneRankRequestDTO requestDTO) {
        PlaceChoiceResponseDTO responseDTO = fortuneService.getStudentPlaceAndOthers(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
