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
                    "물마시기",
                    "운동하기",
                    "닭 먹기",
                    "프로틴먹기",
                    "챌린지이름",
                    "챌린지 이름2",
                    "챌린지,",
                    "헬스장가기,",
                    "밥 먹기,ㅓ",
                    "밥밥"
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
