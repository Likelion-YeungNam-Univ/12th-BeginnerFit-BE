package com.example.beginnerfitbe.challengeparticipant.dto;

import com.example.beginnerfitbe.challengeparticipant.domain.ChallengeParticipant;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeParticipantDTO {


    private Long userId;
    private Long challengeId;
    private String challengeContent;
    private boolean isCompleted;
    private LocalDate challengeCompletedDate;
    private Long successCount; // 친구들의 성공 카운트 추가

    public static ChallengeParticipantDTO fromEntity(ChallengeParticipant challengeParticipant) {
        return ChallengeParticipantDTO.builder()

                .userId(challengeParticipant.getUser().getId())
                .challengeId(challengeParticipant.getChallenge().getChallengeId())
                .challengeContent(challengeParticipant.getChallenge().getChallengeContent())
                .isCompleted(challengeParticipant.isCompleted())
                .challengeCompletedDate(challengeParticipant.getChallengeCompletedDate())
                .build();
    }
}
