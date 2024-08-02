package com.example.beginnerfitbe.alarm.dto;

import com.example.beginnerfitbe.alarm.domain.Alarm;
import com.example.beginnerfitbe.alarm.domain.AlarmType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmDTO {

    private Long alarmId;
    private boolean alarmChecked;
    private LocalDateTime alarmDate;
    private String alarmMessage;
    private Long userId;
    private AlarmType alarmType;

    public static AlarmDTO fromEntity(Alarm alarm) {
        return AlarmDTO.builder()
                .alarmId(alarm.getAlarmId())
                .alarmChecked(alarm.isAlarmChecked())
                .alarmDate(alarm.getAlarmDate())
                .alarmMessage(alarm.getAlarmMessage())
                .userId(alarm.getUser().getId())
                .alarmType(alarm.getAlarmType())
                .build();
    }

}
