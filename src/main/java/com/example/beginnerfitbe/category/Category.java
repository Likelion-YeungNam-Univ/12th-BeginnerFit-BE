package com.example.beginnerfitbe.category;

import com.example.beginnerfitbe.post.domain.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Post> posts;

    @Builder
    public Category(Long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
}
