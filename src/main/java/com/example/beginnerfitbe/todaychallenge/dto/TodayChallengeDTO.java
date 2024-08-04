package com.example.beginnerfitbe.todaychallenge.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayChallengeDTO {

    private Long todaychallengeId;
    private Long challengeId;
    private LocalDate todayChallengeDate;
}
