package com.example.beginnerfitbe.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String profilePictureUrl;

    @Column
    private String exercisePurpose;

    @Column
    private String exercisePart;

    @Column
    private int exerciseTime;

    @Column
    private int exerciseIntensity;

    @Builder
    public User(String email, String name, String password, String profilePictureUrl, String exercisePurpose, String exercisePart, int exerciseTime, int exerciseIntensity) {
        this.email=email;
        this.name = name;
        this.password = password;
        this.profilePictureUrl = profilePictureUrl;
        this.exercisePurpose=exercisePurpose;
        this.exercisePart =exercisePart;
        this.exerciseTime=exerciseTime;
        this.exerciseIntensity = exerciseIntensity;
    }

    public void update(String name, String exercisePurpose, String exercisePart, int exerciseTime, int exerciseIntensity) {
        this.name=name;
        this.exercisePurpose = exercisePurpose;
        this.exercisePart= exercisePart;
        this.exerciseTime=exerciseTime;
        this.exerciseIntensity = exerciseIntensity;
    }

    public void updatePicture(String newPictureUrl){
        this.profilePictureUrl = newPictureUrl;
    }

}
