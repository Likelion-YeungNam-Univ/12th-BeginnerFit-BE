package com.example.beginnerfitbe.weight.controller;

import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.weight.service.WeightRecordService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class WeightRecordController {

    private final WeightRecordService weightRecordService;
    private final JwtUtil jwtUtil;

    //전체 기록 조회
    @GetMapping("/weight-records")
    @Operation(summary = "몸무게 전체 기록 조회", description = "사용자의 전체 몸무게 기록을 조회합니다.")
    public ResponseEntity<?> list(HttpServletRequest request) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(weightRecordService.list(userId));
    }

    //전체 기록 조회
    @GetMapping("/recent-weight-records")
    @Operation(summary = "최근 5번 몸무게 현황 조회", description = "사용자의 최근 5개 몸무게 기록을 조회합니다.")
    public ResponseEntity<?> getRecentWeightRecords(HttpServletRequest request) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(weightRecordService.getRecentWeightRecords(userId));
    }
}
