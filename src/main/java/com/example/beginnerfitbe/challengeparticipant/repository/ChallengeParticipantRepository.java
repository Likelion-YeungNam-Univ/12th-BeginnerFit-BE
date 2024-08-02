package com.example.beginnerfitbe.challengeparticipant.repository;

import com.example.beginnerfitbe.challengeparticipant.domain.ChallengeParticipant;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {

    List<ChallengeParticipant> findByUserIdAndChallengeCompletedDate(Long userId, LocalDate challengeCompletedDate);
    Optional<ChallengeParticipant> findByUserIdAndChallenge_ChallengeIdAndChallengeCompletedDate(Long userId, Long challengeId, LocalDate challengeCompletedDate);
    List<ChallengeParticipant> findByUserIdAndIsCompletedTrue(Long userId);
    long countByUserIdAndIsCompletedTrueAndChallengeCompletedDate(Long userId, LocalDate date);
    boolean existsByChallengeCompletedDate(LocalDate date);

    // 친구들의 성공 카운트를 위한 메소드 추가
    @Query("SELECT COUNT(cp) FROM ChallengeParticipant cp WHERE cp.challenge.challengeId = :challengeId AND cp.user.id IN :userIds AND cp.isCompleted = true")
    long countByChallengeIdAndUserIdIn(@Param("challengeId") Long challengeId, @Param("userIds") List<Long> userIds);

    @Query("SELECT cp FROM ChallengeParticipant cp WHERE cp.user.id = :userId AND cp.challengeCompletedDate = :today")
    List<ChallengeParticipant> findByUserIdAndCompletedDate(Long userId, LocalDate today);

}
