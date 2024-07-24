package com.example.beginnerfitbe.post.domain;

import com.example.beginnerfitbe.category.domain.Category;
import com.example.beginnerfitbe.comment.domain.Comment;
import com.example.beginnerfitbe.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String pictureUrl;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Comment> comments;

    @Column(nullable = false)
    private int declarationCnt;

    @Builder
    public Post(String title, String content, String pictureUrl, LocalDateTime createdAt, User user, Category category, int declarationCnt) {
        this.title = title;
        this.content = content;
        this.pictureUrl = pictureUrl;
        this.createdAt = createdAt;
        this.category = category;
        this.user = user;
        this.declarationCnt = declarationCnt;
    }

    public void update(String title, String content, Category category) {
        this.title=title;
        this.content = content;
        this.category= category;
    }
    public void updatePicture(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    public void updateDeclarationCnt(int newCnt){
        this.declarationCnt = newCnt;
    }
}