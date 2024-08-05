package com.example.beginnerfitbe.challenge.controller;

import com.example.beginnerfitbe.challenge.config.ChallengeScheduler;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challengelist")
@RequiredArgsConstructor
public class ChallengeController {

    @Autowired
    private ChallengeScheduler challengeScheduler;

    @PostMapping("/test")
    @Operation(summary = "오늘 날짜 챌린지 수동 생성 메서드", description = "예시를 만들기 위한 TEST용 api입니다.")
    public String testSchedule() {
        challengeScheduler.testSchedule(); // 수동 호출
        return "Scheduled task executed!";
    }
}
