package com.example.beginnerfitbe.todaychallenge.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todaychallengeId;

    @Column(nullable = false)
    private Long challengeId;

    @Column(nullable = false)
    private LocalDate todayChallengeDate;

    @Builder
    public TodayChallenge(Long challengeId, LocalDate todayChallengeDate) {
        this.challengeId = challengeId;
        this.todayChallengeDate = todayChallengeDate;

    }
}
