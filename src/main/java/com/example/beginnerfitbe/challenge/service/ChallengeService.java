package com.example.beginnerfitbe.challenge.service;

import com.example.beginnerfitbe.challenge.domain.Challenge;
import com.example.beginnerfitbe.challenge.repository.ChallengeRepository;
import com.example.beginnerfitbe.challengeparticipant.domain.ChallengeParticipant;
import com.example.beginnerfitbe.challengeparticipant.repository.ChallengeParticipantRepository;
import com.example.beginnerfitbe.todaychallenge.domain.TodayChallenge;
import com.example.beginnerfitbe.todaychallenge.repository.TodayChallengeRepository;
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
    private final TodayChallengeRepository todayChallengeRepository;

    public List<Challenge> getRandomChallenges(int count) {
        List<Challenge> allChallenges = challengeRepository.findAll();
        Collections.shuffle(allChallenges); // 리스트를 랜덤하게 섞기
        return allChallenges.stream().limit(count).collect(Collectors.toList()); // 상위 count 개수만 반환
    }

    public void assignRandomChallengesToUsers() {
        LocalDate today = LocalDate.now();

        // 오늘 날짜의 ChallengeParticipant가 존재하는지 확인
        boolean hasParticipantsForToday = challengeParticipantRepository.existsByChallengeCompletedDate(today);
        // 오늘 날짜의 TodayChallenge가 존재하는지 확인
        boolean hasTodayChallenges = todayChallengeRepository.existsByTodayChallengeDate(today);

//        // 오늘 날짜에 해당하는 데이터가 존재하면 메서드 종료
//        if (hasParticipantsForToday || hasTodayChallenges) {
//            return; // 메서드 종료
//        }

        List<User> allUsers = userRepository.findAll();
        List<Challenge> randomChallenges = getRandomChallenges(3); // 랜덤 챌린지 3개 가져오기

        // 오늘 날짜에 3개의 랜덤 챌린지를 저장
        for (Challenge challenge : randomChallenges) {
            for (User user : allUsers) {
                // ChallengeParticipant 저장
                ChallengeParticipant participant = ChallengeParticipant.builder()
                        .user(user)
                        .challenge(challenge)
                        .isCompleted(false) // 초기 상태는 미완료
                        .challengeCompletedDate(today) // 오늘 날짜
                        .build();
                challengeParticipantRepository.save(participant); // ChallengeParticipant 저장
            }

            // 오늘의 랜덤 챌린지를 TodayChallengeDTO에 저장
            TodayChallenge todayChallenge = TodayChallenge.builder()
                    .challengeId(challenge.getChallengeId())
                    .todayChallengeDate(today)
                    .build();

            todayChallengeRepository.save(todayChallenge); // TodayChallenge 저장
        }
    }



    public void assignChallengesToUser(User user) {
        LocalDate today = LocalDate.now();

        // 오늘 날짜와 같은 값을 가진 TodayChallenge를 가져옴
        List<TodayChallenge> todayChallenges = todayChallengeRepository.findByTodayChallengeDate(today);

        // 최대 3개의 챌린지를 선택
        List<Long> challengeIds = todayChallenges.stream()
                .map(TodayChallenge::getChallengeId)
                .limit(3)
                .collect(Collectors.toList());

        for (Long challengeId : challengeIds) {
            Challenge challenge = challengeRepository.findById(challengeId)
                    .orElseThrow(() -> new RuntimeException("Challenge not found"));

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
