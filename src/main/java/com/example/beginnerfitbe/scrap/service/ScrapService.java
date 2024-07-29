package com.example.beginnerfitbe.scrap.service;

import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.post.repository.PostRepository;
import com.example.beginnerfitbe.scrap.domain.Scrap;
import com.example.beginnerfitbe.scrap.repository.ScrapRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public StateResponse create(Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));

        Optional <Scrap> scrapOpt = scrapRepository.findByUserAndPost(user, post);

        if(scrapOpt.isPresent()){
            return StateResponse.builder()
                    .code("FAIL")
                    .message("이미 스크랩 했습니다.")
                    .build();
        }
        Scrap scrap = Scrap.builder()
                .user(user)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        scrapRepository.save(scrap);
        return StateResponse.builder()
                .code("SUCCESS")
                .message("게시글을 성공적으로 스크랩했습니다.")
                .build();
    }

}
