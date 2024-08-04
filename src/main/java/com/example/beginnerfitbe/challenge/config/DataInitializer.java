package com.example.beginnerfitbe.challenge.config;

import com.example.beginnerfitbe.challenge.domain.Challenge;
import com.example.beginnerfitbe.challenge.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ChallengeRepository challengeRepository; // Challenge 엔티티를 위한 리포지토리

    @Override
    public void run(String... args) throws Exception {
        // 데이터가 이미 존재하는지 확인
        if (challengeRepository.count() == 0) {
            // 초기 데이터 삽입
            String[] challengeContents = {
                    "아침 10분 스트레칭 하기",
                    "물 2L 이상 마시기",
                    "5000보 걷기",
                    "홈 트레이닝 영상 3개 시청하기",
                    "커피 대신 차 마시기",
                    "야채 반찬 5가지 먹기",
                    "저녁식사 후 30분 홈 트레이닝 하기",
                    "설탕 없는 음료 마시기",
                    "야식 먹지 않기",
                    "엘리베이터 대신 계단 이용하기",
                    "정제 탄수화물 피하기",
                    "건강한 간식으로 과일 1개 먹기",
                    "양치하면서 스쿼트 10개 하기",
                    "닭가슴살 먹기",
                    "아메리카노 대신 보리차 마시기",
                    "3세트 30초씩 플랭크 하기",
                    "몸풀기 스트레칭 10분 하기",
                    "30분동안 스크린 타임 줄이기 (휴대폰, TV)",
                    "1km 조깅하기",
                    "차 타지말고 걷기"
            };

            for (String content : challengeContents) {
                // @Builder를 사용하여 Challenge 객체 생성
                Challenge challenge = Challenge.builder()
                        .challengeContent(content)
                        .build();
                challengeRepository.save(challenge);
            }
        }
    }
}
