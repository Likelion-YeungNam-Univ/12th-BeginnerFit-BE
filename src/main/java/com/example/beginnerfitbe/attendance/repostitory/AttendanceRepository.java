package com.example.beginnerfitbe.attendance.repostitory;

import com.example.beginnerfitbe.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByUserIdAndPresentDate(Long userId, LocalDate presentDate);
    List<Attendance> findByUserId(Long userId);
}


