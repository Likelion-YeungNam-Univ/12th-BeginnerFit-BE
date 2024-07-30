package com.example.beginnerfitbe.challenge.domain;

import com.example.beginnerfitbe.challengeparticipant.domain.ChallengeParticipant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challengeId;

    @Column(nullable = false)
    private String challengeContent;

    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ChallengeParticipant> challengeParticipants = new ArrayList<>();

    @Builder
    public Challenge(String challengeContent) {

        this.challengeContent = challengeContent;

    }

}
