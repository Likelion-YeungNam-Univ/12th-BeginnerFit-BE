package com.example.beginnerfitbe.user.dto;

import com.example.beginnerfitbe.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtherUserDto {

    private Long id;
    private String email;
    private String name;
    private int exercisePurpose;
    private int exercisePart;
    private int exerciseTime;
    private int exerciseIntensity;

    public User toEntity() {
        return User.builder()

                .email(email)
                .name(name)
                .exercisePurpose(exercisePurpose)
                .exercisePart(exercisePart)
                .exerciseTime(exerciseTime)
                .exerciseIntensity(exerciseIntensity)
                .build();
    }
}
