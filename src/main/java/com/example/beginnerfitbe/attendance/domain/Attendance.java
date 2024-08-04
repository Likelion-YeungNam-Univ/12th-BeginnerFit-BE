package com.example.beginnerfitbe.attendance.domain;

import com.example.beginnerfitbe.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @Column(nullable = false)
    private LocalDate presentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

}
