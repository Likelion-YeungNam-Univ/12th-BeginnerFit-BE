package com.example.beginnerfitbe.like.service;

import com.example.beginnerfitbe.like.domain.PostLike;
import com.example.beginnerfitbe.like.repository.PostLikeRepository;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.post.repository.PostRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public String create(Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));

        Optional<PostLike> postLikeOpt = postLikeRepository.findByUserAndPost(user, post);

        if(postLikeOpt.isPresent()){
            return "이미 좋아요를 누르셨습니다.";
        }
        else{
            PostLike postLike = PostLike.builder()
                    .user(user)
                    .post(post)
                    .createdAt(LocalDateTime.now())
                    .build();

            postLikeRepository.save(postLike);
            return "SUCCESS 좋아요가 완료되었습니다.";
        }
    }

    public String delete(Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));

        Optional<PostLike> postLikeOpt = postLikeRepository.findByUserAndPost(user, post);

        if(postLikeOpt.isPresent()){
            //취소
            postLikeRepository.delete(postLikeOpt.get());
            return "SUCCESS 좋아요가 취소되었습니다.";
        }
        return "좋아요가 존재하지 않습니다.";
    }

}
