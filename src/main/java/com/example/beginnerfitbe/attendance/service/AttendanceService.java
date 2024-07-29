package com.example.beginnerfitbe.attendance.service;


import com.example.beginnerfitbe.attendance.domain.Attendance;
import com.example.beginnerfitbe.attendance.dto.AttendanceCountDTO;
import com.example.beginnerfitbe.attendance.dto.AttendanceDateDTO;
import com.example.beginnerfitbe.attendance.repostitory.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public List<AttendanceDateDTO> getAttendanceDatesByUserId(Long userId) {
        List<Attendance> attendances = attendanceRepository.findByUserId(userId);
        return attendances.stream()
                .map(attendance -> new AttendanceDateDTO(attendance.getPresentDate())) // AttendanceDateDTO로 변환
                .collect(Collectors.toList());
    }

    public List<AttendanceCountDTO> getMonthlyAttendanceCountByUserId(Long userId) {
        List<Attendance> attendances = attendanceRepository.findByUserId(userId);

        // 출석 데이터를 월별로 그룹화
        Map<String, Long> monthlyCount = new HashMap<>();
        for (Attendance attendance : attendances) {
            LocalDate date = attendance.getPresentDate();
            String key = date.getYear() + "-" + date.getMonthValue(); // "년-월" 형식으로 키 생성
            monthlyCount.put(key, monthlyCount.getOrDefault(key, 0L) + 1);
        }

        // 결과를 DTO 리스트로 변환
        List<AttendanceCountDTO> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : monthlyCount.entrySet()) {
            String[] parts = entry.getKey().split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            result.add(new AttendanceCountDTO(year, month, entry.getValue()));
        }

        return result;
    }

}
