package com.example.beginnerfitbe.user.dto;

import com.example.beginnerfitbe.user.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String password;
    private double height;
    private double weight;
    private double targetWeight;
    private String date;
    private String targetDate;
    private int exerciseTime;
    private List<String> exerciseGoals;
    private List<String> concernedAreas;
    private List<String> exerciseIntensity;

    public UserDto(Long id, String email, String name, String password, double height, double weight, double targetWeight, String date, String targetDate, int exerciseTime, List<String> exerciseGoals, List<String> concernedAreas, List<String> exerciseIntensity) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.height = height;
        this.weight = weight;
        this.targetWeight = targetWeight;
        this.date = date;
        this.targetDate = targetDate;
        this.exerciseTime = exerciseTime;
        this.exerciseGoals = exerciseGoals;
        this.concernedAreas = concernedAreas;
        this.exerciseIntensity = exerciseIntensity;
    }

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword(),
                user.getHeight(),
                user.getWeight(),
                user.getTargetWeight(),
                user.getDate(),
                user.getTargetDate(),
                user.getExerciseTime(),
                user.getExerciseGoals(),
                user.getConcernedAreas(),
                user.getExerciseIntensity()
        );
    }


}
