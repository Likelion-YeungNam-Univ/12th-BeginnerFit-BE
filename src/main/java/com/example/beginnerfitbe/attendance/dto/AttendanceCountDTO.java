package com.example.beginnerfitbe.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceCountDTO {
    private int year; // 연도
    private int month; // 월
    private long count; // 출석 횟수
}