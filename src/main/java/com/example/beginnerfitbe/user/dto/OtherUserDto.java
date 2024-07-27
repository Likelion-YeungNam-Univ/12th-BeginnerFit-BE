package com.example.beginnerfitbe.user.dto;

import com.example.beginnerfitbe.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OtherUserDto {

    private Long id;
    private String email;
    private String name;
    private double height;
    private double weight;
    private double targetWeight;
    private String date;
    private String targetDate;
    private int exerciseTime;
    private List<String> exercisePurpose;
    private List<String> exercisePart;
    private List<String> exerciseIntensity;

    public User toEntity() {
        return User.builder()
                .email(email)
                .name(name)
                .height(height)
                .weight(weight)
                .targetWeight(targetWeight)
                .date(date)
                .targetDate(targetDate)
                .exerciseTime(exerciseTime)
                .exerciseGoals(exercisePurpose)
                .concernedAreas(exercisePart)
                .exerciseIntensity(exerciseIntensity)
                .build();
    }
}
