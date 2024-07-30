package com.example.beginnerfitbe.user.domain;

import com.example.beginnerfitbe.attendance.domain.Attendance;
import com.example.beginnerfitbe.challengeparticipant.domain.ChallengeParticipant;
import com.example.beginnerfitbe.declaration.domain.Declaration;
import com.example.beginnerfitbe.like.domain.PostLike;
import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.scrap.domain.Scrap;
import com.example.beginnerfitbe.weight.domain.WeightRecord;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column
    private String profileUrl;

    @Column
    private double height;

    @Column
    private double weight;

    @Column
    private double targetWeight;

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


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<WeightRecord> weightRecords;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ChallengeParticipant> challengeParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Playlist> playlists;

    //연결 entity
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Scrap> scraps;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostLike> postLikes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Declaration> declarations;

    @Builder
    public User(String email, String name, String password, String profileUrl, double height, double weight, double targetWeight, String date, String targetDate, int exerciseTime, List<String> exerciseIntensity, List<String> exerciseGoals, List<String> concernedAreas) {
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

    public void updateHealthInfo(double height, double weight, double targetWeight, String date, String targetDate, int exerciseTime, List<String> exerciseIntensity, List<String> exerciseGoals, List<String> concernedAreas) {
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
    public void updatePassword(String newPassword){this.password = newPassword;}
}
