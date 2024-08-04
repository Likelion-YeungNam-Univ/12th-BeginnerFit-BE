package com.example.beginnerfitbe.challenge.config;

import com.example.beginnerfitbe.challenge.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class ChallengeScheduler {

    @Autowired
    private ChallengeService challengeService; // 위에서 만든 메서드를 포함하는 서비스

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void scheduleRandomChallengeAssignment() {
        challengeService.assignRandomChallengesToUsers();
    }

    // 테스트용 메서드 추가
    public void testSchedule() {
        scheduleRandomChallengeAssignment(); // 메서드 직접 호출
    }
}