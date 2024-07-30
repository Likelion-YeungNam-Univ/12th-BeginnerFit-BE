package com.example.beginnerfitbe.challengeparticipant.service;

import com.example.beginnerfitbe.challengeparticipant.domain.ChallengeParticipant;
import com.example.beginnerfitbe.challengeparticipant.dto.ChallengeParticipantDTO;
import com.example.beginnerfitbe.challengeparticipant.repository.ChallengeParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeParticipantService {

    private final ChallengeParticipantRepository challengeParticipantRepository;

    @Transactional(readOnly = true)
    public List<ChallengeParticipantDTO> getUserChallenges(Long userId) {


        // 현재 날짜
        LocalDate currentDate = LocalDate.now();

        // userId와 현재 날짜로 ChallengeParticipant 리스트 반환
        List<ChallengeParticipant> participants = challengeParticipantRepository.findByUserIdAndChallengeCompletedDate(userId, currentDate);

        // DTO로 변환하여 반환
        return participants.stream()
                .map(ChallengeParticipantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void completeChallenge(Long userId, Long challengeId) {
        LocalDate currentDate = LocalDate.now();

        // 오늘 날짜이면서 주어진 challengeId와 userId에 해당하는 참가자 찾기
        ChallengeParticipant participant = challengeParticipantRepository
                .findByUserIdAndChallenge_ChallengeIdAndChallengeCompletedDate(userId, challengeId, currentDate)
                .orElseThrow(() -> new RuntimeException("Challenge participant not found")); // 예외 처리

        // isCompleted를 true로 변경
        participant.setCompleted(true); // setter 메서드 필요
        challengeParticipantRepository.save(participant); // 변경사항 저장
    }

    public void notcompleteChallenge(Long userId, Long challengeId) {
        LocalDate currentDate = LocalDate.now();

        // 오늘 날짜이면서 주어진 challengeId와 userId에 해당하는 참가자 찾기
        ChallengeParticipant participant = challengeParticipantRepository
                .findByUserIdAndChallenge_ChallengeIdAndChallengeCompletedDate(userId, challengeId, currentDate)
                .orElseThrow(() -> new RuntimeException("Challenge participant not found")); // 예외 처리

        // isCompleted를 true로 변경
        participant.setCompleted(false); // setter 메서드 필요
        challengeParticipantRepository.save(participant); // 변경사항 저장
    }

    public List<ChallengeParticipantDTO> getCompletedChallengesByDate(Long userId, int year, int month) {
        List<ChallengeParticipant> participants = challengeParticipantRepository.findByUserIdAndIsCompletedTrue(userId);

        return participants.stream()
                .filter(participant -> {
                    LocalDate completedDate = participant.getChallengeCompletedDate();
                    return completedDate.getYear() == year && completedDate.getMonthValue() == month;
                })
                .map(ChallengeParticipantDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
