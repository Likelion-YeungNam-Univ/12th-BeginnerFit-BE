package com.example.beginnerfitbe.scrap.domain;


import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Post post;

    @ManyToOne(fetch = LAZY)
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Scrap(Post post, User user, LocalDateTime createdAt) {
        this.post = post;
        this.user = user;
        this.createdAt = createdAt;
    }
}
