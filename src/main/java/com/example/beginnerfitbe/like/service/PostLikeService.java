package com.example.beginnerfitbe.like.service;

import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.like.domain.PostLike;
import com.example.beginnerfitbe.like.dto.PostLikeDto;
import com.example.beginnerfitbe.like.repository.PostLikeRepository;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.post.dto.PostDto;
import com.example.beginnerfitbe.post.repository.PostRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public StateResponse create(Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));

        Optional<PostLike> postLikeOpt = postLikeRepository.findByUserAndPost(user, post);

        if(postLikeOpt.isPresent()){
            return StateResponse.builder()
                    .code("FAIL")
                    .message("이미 좋아요를 누르셨습니다.")
                    .build();
        } else {
            PostLike postLike = PostLike.builder()
                    .user(user)
                    .post(post)
                    .createdAt(LocalDateTime.now())
                    .build();

            postLikeRepository.save(postLike);
            return StateResponse.builder()
                    .code("SUCCESS")
                    .message("좋아요가 완료되었습니다.")
                    .build();
        }
    }

    public StateResponse delete(Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));

        Optional<PostLike> postLikeOpt = postLikeRepository.findByUserAndPost(user, post);

        if(postLikeOpt.isPresent()){
            //취소
            postLikeRepository.delete(postLikeOpt.get());
            return StateResponse.builder()
                    .code("SUCCESS")
                    .message("좋아요가 취소되었습니다.")
                    .build();
        }
        return StateResponse.builder()
                .code("FAIL")
                .message("좋아요가 존재하지 않습니다.")
                .build();
    }

    public List<PostLikeDto> getLikesByUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));

        return postLikeRepository.findByUser(user).stream()
                .map(PostLikeDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PostLikeDto> getLikesByPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));
        return postLikeRepository.findByPost(post).stream()
                .map(PostLikeDto::fromEntity)
                .collect(Collectors.toList());
    }

    public boolean liked(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));

        Optional<PostLike> postLikeOpt = postLikeRepository.findByUserAndPost(user, post);
        return postLikeOpt.isPresent();

    }
}
