package com.example.beginnerfitbe.challengeparticipant.repository;

import com.example.beginnerfitbe.challengeparticipant.domain.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {

    List<ChallengeParticipant> findByUserIdAndChallengeCompletedDate(Long userId, LocalDate challengeCompletedDate);
    Optional<ChallengeParticipant> findByUserIdAndChallenge_ChallengeIdAndChallengeCompletedDate(Long userId, Long challengeId, LocalDate challengeCompletedDate);
    List<ChallengeParticipant> findByUserIdAndIsCompletedTrue(Long userId);

}
