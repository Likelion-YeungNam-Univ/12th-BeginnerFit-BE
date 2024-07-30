package com.example.beginnerfitbe.challengeparticipant.domain;

import com.example.beginnerfitbe.challenge.domain.Challenge;
import com.example.beginnerfitbe.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
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
    private LocalDate challengeCompletedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challengeId", referencedColumnName = "challengeId")
    private Challenge challenge;

    @Builder
    public ChallengeParticipant(User user, Challenge challenge, boolean isCompleted, LocalDate challengeCompletedDate) {
        this.user = user;
        this.challenge = challenge;
        this.isCompleted = isCompleted;
        this.challengeCompletedDate = challengeCompletedDate.now();
    }


}
