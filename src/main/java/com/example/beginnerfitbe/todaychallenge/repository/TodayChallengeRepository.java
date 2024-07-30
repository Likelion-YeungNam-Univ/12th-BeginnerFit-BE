package com.example.beginnerfitbe.todaychallenge.repository;

import com.example.beginnerfitbe.todaychallenge.domain.TodayChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodayChallengeRepository extends JpaRepository<TodayChallenge, Long> {

    boolean existsByTodayChallengeDate(LocalDate date);
    List<TodayChallenge> findByTodayChallengeDate(LocalDate date);

}
