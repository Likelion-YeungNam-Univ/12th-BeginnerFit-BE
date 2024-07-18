package com.example.beginnerfitbe.category.domain;

import com.example.beginnerfitbe.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(nullable = false, unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Post> posts;

    @Builder
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}

