package com.example.beginnerfitbe.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column
    private String profileUrl;

    @Column
    private int height;

    @Column
    private int weight;

    @Column
    private int targetWeight;

    @Column
    private String date;

    @Column
    private String targetDate;

    @Column
    private int exerciseTime;

    @ElementCollection
    private List<String> exerciseIntensity;

    @ElementCollection
    private List<String> exerciseGoals;

    @ElementCollection
    private List<String> concernedAreas;


//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<WeightRecord> weightRecords;

    @Builder
    public User(String email, String name, String password, String profileUrl, int height, int weight, int targetWeight, String date, String targetDate, int exerciseTime, List<String> exerciseIntensity, List<String> exerciseGoals, List<String> concernedAreas) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.profileUrl=profileUrl;
        this.height = height;
        this.weight = weight;
        this.date= date;
        this.targetWeight = targetWeight;
        this.targetDate = targetDate;
        this.exerciseTime = exerciseTime;
        this.exerciseIntensity = exerciseIntensity;
        this.exerciseGoals = exerciseGoals;
        this.concernedAreas = concernedAreas;
    }

    public void updateHealthInfo(int height, int weight, int targetWeight, String date, String targetDate, int exerciseTime, List<String> exerciseIntensity, List<String> exerciseGoals, List<String> concernedAreas) {
        this.height = height;
        this.weight = weight;
        this.targetWeight = targetWeight;
        this.date = date;
        this.targetDate = targetDate;
        this.exerciseTime = exerciseTime;
        this.exerciseIntensity = exerciseIntensity;
        this.exerciseGoals = exerciseGoals;
        this.concernedAreas = concernedAreas;
    }
    public void updateName(String newName){
        this.name= newName;
    }
}
