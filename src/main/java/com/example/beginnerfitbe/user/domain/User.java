package com.example.beginnerfitbe.user.domain;

import com.example.beginnerfitbe.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private int exercisePurpose;

    @Column
    private int exercisePart;

    @Column
    private int exerciseTime;

    @Column
    private int exerciseIntensity;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Post> posts;


    @Builder
    public User(String email, String name, String password, int exercisePurpose, int exercisePart, int exerciseTime, int exerciseIntensity) {
        this.email=email;
        this.name = name;
        this.password = password;
        this.exercisePurpose=exercisePurpose;
        this.exercisePart =exercisePart;
        this.exerciseTime=exerciseTime;
        this.exerciseIntensity = exerciseIntensity;
    }

    public void update(String name, String password, int exercisePurpose, int exercisePart, int exerciseTime, int exerciseIntensity) {
        this.name=name;
        this.password = password;
        this.exercisePurpose = exercisePurpose;
        this.exercisePart= exercisePart;
        this.exerciseTime=exerciseTime;
        this.exerciseIntensity = exerciseIntensity;
    }
}
