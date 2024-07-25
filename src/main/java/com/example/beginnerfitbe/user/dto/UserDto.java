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
    private int height;
    private int weight;
    private int targetWeight;
    private String date;
    private String targetDate;
    private int exerciseTime;
    private List<String> exercisePurpose;
    private List<String> exercisePart;
    private List<String> exerciseIntensity;

    public UserDto(Long id, String email, String name, String password, int height, int weight, int targetWeight, String date, String targetDate, int exerciseTime, List<String> exercisePurpose, List<String> exercisePart, List<String> exerciseIntensity) {
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
        this.exercisePurpose = exercisePurpose;
        this.exercisePart = exercisePart;
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
                user.getExerciseIntensity(),
                user.getExerciseGoals(),
                user.getConcernedAreas()
        );
    }
}
