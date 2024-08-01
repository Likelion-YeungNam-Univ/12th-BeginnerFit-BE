package com.example.beginnerfitbe.notification.domain;

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
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private boolean notificationCheck;

    @Column(nullable = false)
    private LocalDateTime notificationDate;

    @Column(nullable = false)
    private String notificationMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User users;

    @Builder
    public Notification(User users, boolean notificationCheck, String notificationMessage ) {
        this.users = users;
        this.notificationCheck = notificationCheck;
        this.notificationDate = LocalDateTime.now();
        this.notificationMessage = notificationMessage;
    }


}
