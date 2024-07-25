package com.example.beginnerfitbe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor

public class HealthInfoReqDto {
    private String email;
    private int height;
    private int weight;
    private int targetWeight;
    private String date;
    private String targetDate;
    private int exerciseTime;
    private List<String> exerciseIntensity;
    private List<String> exerciseGoals;
    private List<String> concernedAreas;
}
