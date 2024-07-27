package com.example.beginnerfitbe.weight.dto;

import com.example.beginnerfitbe.weight.domain.WeightRecord;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WeightRecordDto {
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
    private double weight;
    private double targetWeight;

    public WeightRecordDto(Long userId, String userName, LocalDateTime createdAt, double weight, double targetWeight) {
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
        this.weight = weight;
        this.targetWeight =targetWeight;
    }

    public static WeightRecordDto fromEntity(WeightRecord weightRecord){
       return new WeightRecordDto(
               weightRecord.getUser().getId(),
               weightRecord.getUser().getName(),
               weightRecord.getDate(),
               weightRecord.getWeight(),
               weightRecord.getUser().getTargetWeight()
       );
    }
}
