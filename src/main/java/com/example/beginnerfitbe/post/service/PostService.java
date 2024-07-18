package com.example.beginnerfitbe.post.service;

import com.example.beginnerfitbe.category.domain.Category;
import com.example.beginnerfitbe.category.repository.CategoryRepository;
import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.post.dto.PostCreateDto;
import com.example.beginnerfitbe.post.dto.PostDto;
import com.example.beginnerfitbe.post.repository.PostRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    //전체 글 조회
    public List<PostDto> list(){
        return postRepository.findAll().stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }
    //postId로 조회
    public PostDto read(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id= " + postId));
        return PostDto.fromEntity(post);
    }
    //내가 작성한 글 조회
    public List<PostDto> me(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));
        List<Post> posts= postRepository.findAllByUser(user);
        return posts.stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }
    public List<PostDto> getPostsByCategoryName(String categoryName){
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(() -> new IllegalArgumentException("not found category"));
        List<Post> posts = postRepository.findPostsByCategory(category);

        return posts.stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }
    @Transactional
    public ResponseEntity<StateResponse> delete(Long postId, Long id){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));
        Long userId = post.getUser().getId();
        if(!userId.equals(id)) throw new IllegalArgumentException("작성자만 글을 삭제할 수 있습니다.");

        postRepository.delete(post);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("글을 성공적으로 삭제했습니다.").build());
    }
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
