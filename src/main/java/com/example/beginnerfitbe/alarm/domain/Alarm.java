package com.example.beginnerfitbe.alarm.domain;

import com.example.beginnerfitbe.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
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

    @Builder
    public Alarm(User user, boolean alarmChecked, String alarmMessage ) {
        this.user = user;
        this.alarmChecked = alarmChecked;
        this.alarmDate = LocalDateTime.now();
        this.alarmMessage = alarmMessage;
    }

}
