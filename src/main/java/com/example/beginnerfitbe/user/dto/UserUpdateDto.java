package com.example.beginnerfitbe.user.dto;

import com.example.beginnerfitbe.user.domain.User;
import lombok.Data;


@Data
public class UserUpdateDto {
    private String name;
    private String password;
    private int exercisePurpose;
    private int exercisePart;
    private int exerciseTime;
    private int exerciseIntensity;

    public User toEntity(){
        return User.builder()
                .name(name)
                .password(password)
                .exercisePurpose(exercisePurpose)
                .exercisePart(exercisePart)
                .exerciseTime(exerciseTime)
                .exerciseIntensity(exerciseIntensity)
                .build();
    }

}
