package com.example.beginnerfitbe.weight.repository;

import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.weight.domain.WeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {
    List<WeightRecord> findByUserOrderByDateAsc(User user);
    List<WeightRecord> findTop5ByUserOrderByDateDesc(User user);
}
