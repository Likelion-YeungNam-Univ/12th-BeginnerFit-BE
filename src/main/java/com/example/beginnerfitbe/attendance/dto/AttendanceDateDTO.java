package com.example.beginnerfitbe.attendance.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDateDTO {

    private LocalDate presentDate;
}
