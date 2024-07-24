package com.example.beginnerfitbe.user.dto;

import lombok.Data;


@Data
public class UserUpdateDto {
    private String name;
    private String exercisePurpose;
    private String exercisePart;
    private int exerciseTime;
    private int exerciseIntensity;


}
