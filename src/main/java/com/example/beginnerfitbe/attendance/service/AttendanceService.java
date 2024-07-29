package com.example.beginnerfitbe.attendance.service;


import com.example.beginnerfitbe.attendance.domain.Attendance;
import com.example.beginnerfitbe.attendance.dto.AttendanceDateDTO;
import com.example.beginnerfitbe.attendance.repostitory.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

}
