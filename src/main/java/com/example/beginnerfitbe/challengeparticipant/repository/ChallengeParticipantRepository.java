package com.example.beginnerfitbe.challengeparticipant.repository;

import com.example.beginnerfitbe.challengeparticipant.domain.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {
}
