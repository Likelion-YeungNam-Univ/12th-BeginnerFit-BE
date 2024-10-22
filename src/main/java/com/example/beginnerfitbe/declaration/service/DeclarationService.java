package com.example.beginnerfitbe.declaration.service;

import com.example.beginnerfitbe.declaration.domain.Declaration;
import com.example.beginnerfitbe.declaration.dto.DeclarationDto;
import com.example.beginnerfitbe.declaration.dto.DeclarationReqDto;
import com.example.beginnerfitbe.declaration.repository.DeclarationRepository;
import com.example.beginnerfitbe.declaration.util.DeclarationReason;
import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.post.repository.PostRepository;
import com.example.beginnerfitbe.post.service.PostService;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeclarationService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final DeclarationRepository declarationRepository;
    private final PostService postService;

    public StateResponse create(Long userId, Long postId, DeclarationReqDto declarationReqDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (user.getId().equals(post.getUser().getId())) {
            throw new IllegalArgumentException("본인 게시물 신고 불가능");
        }

        DeclarationReason reason;
        try {
            reason = DeclarationReason.valueOf(declarationReqDto.getReason());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid declaration reason");
        }

        Optional<Declaration> existingReport = declarationRepository.findByUserAndPost(user, post);

        //신고사유만 변경
        if (existingReport.isPresent()) {
            Declaration report = existingReport.get();
            report.updateReason(reason);

            declarationRepository.save(report);
            return StateResponse.builder()
                    .code("SUCCESS")
                    .message("신고 사유가 변경되었습니다.")
                    .build();
        } else {

            if (post.getDeclarations().size() >= 9) {
                postService.delete(postId, post.getUser().getId()); // 게시글 삭제
                return StateResponse.builder()
                        .code("SUCCESS")
                        .message("신고 10번 누적으로 게시글이 삭제되었습니다.")
                        .build();
            }

            Declaration declaration = Declaration.builder()
                    .post(post)
                    .user(user)
                    .reason(reason)
                    .build();
            declarationRepository.save(declaration);
            return StateResponse.builder()
                    .code("SUCCESS")
                    .message("SUCCESS 게시글이 정상적으로 신고되었습니다.")
                    .build();
        }
    }

    public List<DeclarationDto> list(){
        return declarationRepository.findAll().stream()
                .map(DeclarationDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<DeclarationDto> getDeclarationsByPost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return declarationRepository.findByPost(post).stream()
                .map(DeclarationDto::fromEntity)
                .collect(Collectors.toList());
    }

    public StateResponse delete(Long userId, Long postId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Optional<Declaration> existingReport = declarationRepository.findByUserAndPost(user, post);

        if (existingReport.isPresent()) {
            declarationRepository.delete(existingReport.get());
            return StateResponse.builder()
                    .code("SUCCESS")
                    .message("신고가 취소되었습니다.")
                    .build();
        }
        return StateResponse.builder()
                .code("FAIL")
                .message("신고 내역이 없습니다.")
                .build();
    }

}
