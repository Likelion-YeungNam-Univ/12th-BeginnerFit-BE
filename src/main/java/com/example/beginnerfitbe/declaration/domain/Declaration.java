package com.example.beginnerfitbe.declaration.domain;

import com.example.beginnerfitbe.declaration.util.ReportReason;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Declaration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    @Builder
    private Declaration (User user, Post post, ReportReason reason){
        this.user = user;
        this.post = post;
        this.reason = reason;
    }
}
