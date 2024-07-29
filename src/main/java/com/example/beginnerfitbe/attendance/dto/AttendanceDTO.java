package com.example.beginnerfitbe.attendance.dto;

import com.example.beginnerfitbe.attendance.domain.Attendance;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDTO {

    private Long attendanceId;
    private Long userId;
    private LocalDate presentDate;

    public static AttendanceDTO fromEntity(Attendance attendance) {
        return AttendanceDTO.builder()
                .attendanceId(attendance.getAttendanceId())
                .userId(attendance.getUser().getId())
                .presentDate(attendance.getPresentDate())
                .build();
    }
}
