package com.example.beginnerfitbe.post.service;

import com.example.beginnerfitbe.category.domain.Category;
import com.example.beginnerfitbe.category.repository.CategoryRepository;
import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.post.dto.PostCreateDto;
import com.example.beginnerfitbe.post.repository.PostRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ResponseEntity<StateResponse> create(Long userId, PostCreateDto postCreateDto){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));

        Category category = categoryRepository.findByCategoryName(postCreateDto.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException("not found category"));

        Post post = Post.builder()
                .title(postCreateDto.getTitle())
                .content(postCreateDto.getContent())
                .createdAt(LocalDateTime.now())
                .user(user)
                .category(category)
                .build();

        postRepository.save(post);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("글을 성공적으로 생성했습니다.").build());
    }

}
