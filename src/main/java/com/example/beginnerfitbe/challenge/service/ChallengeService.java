package com.example.beginnerfitbe.challenge.service;

import com.example.beginnerfitbe.challenge.domain.Challenge;
import com.example.beginnerfitbe.challenge.repository.ChallengeRepository;
import com.example.beginnerfitbe.challengeparticipant.domain.ChallengeParticipant;
import com.example.beginnerfitbe.challengeparticipant.repository.ChallengeParticipantRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;

    public List<Challenge> getRandomChallenges(int count) {
        List<Challenge> allChallenges = challengeRepository.findAll();
        Collections.shuffle(allChallenges); // 리스트를 랜덤하게 섞기
        return allChallenges.stream().limit(count).collect(Collectors.toList()); // 상위 count 개수만 반환
    }

    public void assignRandomChallengesToUsers() {
        List<User> allUsers = userRepository.findAll();
        List<Challenge> randomChallenges = getRandomChallenges(3); // 랜덤 챌린지 3개 가져오기

        for (User user : allUsers) {
            for (Challenge challenge : randomChallenges) {
                ChallengeParticipant participant = ChallengeParticipant.builder()
                        .user(user)
                        .challenge(challenge)
                        .isCompleted(false) // 초기 상태는 미완료
                        .challengeCompletedDate(LocalDate.now()) // 오늘 날짜
                        .build();
                challengeParticipantRepository.save(participant); // ChallengeParticipant 저장
            }
        }
    }
}
