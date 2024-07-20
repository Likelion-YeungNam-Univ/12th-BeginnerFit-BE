package com.example.beginnerfitbe.user.dto;

import lombok.Data;


@Data
public class UserUpdateDto {
    private String name;
    private int exercisePurpose;
    private int exercisePart;
    private int exerciseTime;
    private int exerciseIntensity;


}
