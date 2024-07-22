package com.example.beginnerfitbe.user.dto;

import com.example.beginnerfitbe.post.dto.PostDto;
import com.example.beginnerfitbe.user.domain.User;
import lombok.Data;
import lombok.Getter;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String password;
    private String profilePictureUrl;
    private int exercisePurpose;
    private int exercisePart;
    private int exerciseTime;
    private int exerciseIntensity;

    public UserDto(Long id, String email, String name, String password, String profilePictureUrl, int exercisePurpose, int exercisePart, int exerciseTime, int exerciseIntensity) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.profilePictureUrl = profilePictureUrl;
        this.exercisePurpose = exercisePurpose;
        this.exercisePart = exercisePart;
        this.exerciseTime = exerciseTime;
        this.exerciseIntensity = exerciseIntensity;
    }

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword(),
                user.getProfilePictureUrl(),
                user.getExercisePurpose(),
                user.getExercisePart(),
                user.getExerciseTime(),
                user.getExerciseIntensity()
        );
    }
}
