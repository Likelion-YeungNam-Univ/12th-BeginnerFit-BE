package com.example.beginnerfitbe.alarm.service;

import com.example.beginnerfitbe.alarm.domain.Alarm;
import com.example.beginnerfitbe.alarm.domain.AlarmType;
import com.example.beginnerfitbe.alarm.repository.AlarmRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    public void createAlarm(User receiver, String alarmMessage, AlarmType alarmType) {
        Alarm alarm = Alarm.builder()
                .user(receiver)
                .alarmChecked(false)
                .alarmMessage(alarmMessage)
                .alarmType(alarmType) // 알림 타입 추가
                .build();
        alarmRepository.save(alarm);
    }


    public List<Alarm> getAlarmsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return alarmRepository.findByUser(user);
    }
}
