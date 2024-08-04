package com.example.beginnerfitbe.alarm.domain;

import com.example.beginnerfitbe.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @Column(nullable = false)
    private boolean alarmChecked;

    @Column(nullable = false)
    private LocalDateTime alarmDate;

    @Column(nullable = false)
    private String alarmMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    private AlarmType alarmType; // 추가된 enum 필드

    @Builder
    public Alarm(User user, boolean alarmChecked, String alarmMessage, AlarmType alarmType) {
        this.user = user;
        this.alarmChecked = alarmChecked;
        this.alarmDate = LocalDateTime.now();
        this.alarmMessage = alarmMessage;
        this.alarmType = alarmType;
    }
}
