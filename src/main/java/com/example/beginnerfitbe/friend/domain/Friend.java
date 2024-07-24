package com.example.beginnerfitbe.friend.domain;

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
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendId;

    @Column(nullable = false)
    private boolean isAccepted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverId")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId")
    private User sender;

    @Builder
    public Friend(User sender, User receiver, boolean isAccepted, LocalDateTime createdAt) {
        this.sender = sender;
        this.receiver = receiver;
        this.isAccepted = isAccepted;
        this.createdAt = LocalDateTime.now();
    }

    public void accept() {
        this.isAccepted = true;
    }

    public void reject() {
        this.isAccepted = false;
    }

}

