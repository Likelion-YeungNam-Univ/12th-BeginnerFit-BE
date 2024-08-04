package com.example.beginnerfitbe.weight.domain;

import com.example.beginnerfitbe.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeightRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime date;

    @Column(nullable = false)
    private double weight;

    @Builder
    public WeightRecord(User user, LocalDateTime date, double weight) {
        this.user = user;
        this.date = date;
        this.weight = weight;
    }
}
