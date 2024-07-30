package com.example.beginnerfitbe.challengeparticipant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRankingDto {
    private Long userId; // 친구의 ID
    private String name; // 친구의 이름
    private String profileUrl;
    private int completedCount; // 친구의 완료된 챌린지 수
    private int rank; // 친구의 순위
}