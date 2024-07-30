package com.example.beginnerfitbe.challengeparticipant.domain;

import com.example.beginnerfitbe.challenge.domain.Challenge;
import com.example.beginnerfitbe.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challengeParticipantId;

    @Column(nullable = false)
    private boolean isCompleted;

    @Column(nullable = false)
    private LocalDate ChallengeCompletedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challengeId")
    private Challenge challenge;

    @Builder
    public ChallengeParticipant(User user, Challenge challenge, boolean isCompleted, LocalDate ChallengeCompletedDate) {
        this.user = user;
        this.challenge = challenge;
        this.isCompleted = isCompleted;
        this.ChallengeCompletedDate = ChallengeCompletedDate.now();
    }


}
