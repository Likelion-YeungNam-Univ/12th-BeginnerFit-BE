package com.example.beginnerfitbe.attendance.controller;

import com.example.beginnerfitbe.attendance.dto.AttendanceCountDTO;
import com.example.beginnerfitbe.attendance.dto.AttendanceDateDTO;
import com.example.beginnerfitbe.attendance.service.AttendanceService;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final JwtUtil jwtUtil;

    @GetMapping("/dates")
    @Operation(summary = "출석 정보 확인 메서드", description = "내 출석정보를 확인합니다.")
    public ResponseEntity<List<AttendanceDateDTO>> getAttendanceDates(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7)); // JWT에서 사용자 ID 추출

        List<AttendanceDateDTO> attendanceDates = attendanceService.getAttendanceDatesByUserId(userId);
        return ResponseEntity.ok(attendanceDates); // AttendanceDateDTO 리스트 반환
    }

    @GetMapping("/monthly-count")
    @Operation(summary = "한달 출석 횟수 확인 메서드", description = "한달에 몇 번 출석했는지 확인합니다.")
    public ResponseEntity<List<AttendanceCountDTO>> getMonthlyAttendanceCount(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7)); // JWT에서 사용자 ID 추출

        List<AttendanceCountDTO> monthlyCounts = attendanceService.getMonthlyAttendanceCountByUserId(userId);
        return ResponseEntity.ok(monthlyCounts); // AttendanceCountDTO 리스트 반환
    }
}
