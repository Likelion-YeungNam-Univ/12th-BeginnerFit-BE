package com.example.beginnerfitbe.alarm.controller;

import com.example.beginnerfitbe.alarm.domain.Alarm;
import com.example.beginnerfitbe.alarm.dto.AlarmDTO;
import com.example.beginnerfitbe.alarm.service.AlarmService;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    private final JwtUtil jwtUtil;  // JwtUtil 주입

    @GetMapping
    public ResponseEntity<List<AlarmDTO>> getAlarms(HttpServletRequest request) {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        // 사용자 ID로 알림 조회
        List<Alarm> alarms = alarmService.getAlarmsByUserId(userId);
        List<AlarmDTO> alarmDTOs = alarms.stream()
                .map(AlarmDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(alarmDTOs);
    }


}
