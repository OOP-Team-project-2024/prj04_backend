package com.oop.puangJumJum.controller;


import com.oop.puangJumJum.dto.request.FortuneRankRequestDTO;
import com.oop.puangJumJum.dto.response.FortuneRankResponseDTO;
import com.oop.puangJumJum.service.FortuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fortune")
public class FortuneController {

    @Autowired
    private FortuneService fortuneService;

    // 운세 순위 조회 API
    @PostMapping("/rank")
    public FortuneRankResponseDTO getFortuneRank(@RequestBody FortuneRankRequestDTO requestDTO) {
        return fortuneService.getFortuneRank(requestDTO);
    }
}
