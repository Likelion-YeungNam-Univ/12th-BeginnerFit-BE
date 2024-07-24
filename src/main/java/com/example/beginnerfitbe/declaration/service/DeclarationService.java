package com.example.beginnerfitbe.declaration.service;

import com.example.beginnerfitbe.declaration.domain.Declaration;
import com.example.beginnerfitbe.declaration.dto.DeclarationDto;
import com.example.beginnerfitbe.declaration.dto.DeclarationReqDto;
import com.example.beginnerfitbe.declaration.repository.DeclarationRepository;
import com.example.beginnerfitbe.declaration.util.DeclarationReason;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.post.repository.PostRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeclarationService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final DeclarationRepository declarationRepository;

    public DeclarationDto create(Long userId, Long postId, DeclarationReqDto declarationReqDto) {
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

        //이미 한번 신고함 -> 사유만 변경
        if (existingReport.isPresent()) {
            Declaration report = existingReport.get();
            report.updateReason(reason);
            declarationRepository.save(report);
            return DeclarationDto.fromEntity(report);
        } else {
            post.updateDeclarationCnt(post.getDeclarationCnt() + 1);
            Declaration declaration = Declaration.builder()
                    .post(post)
                    .user(user)
                    .reason(reason)
                    .build();
            declarationRepository.save(declaration);
            return DeclarationDto.fromEntity(declaration);
        }
    }


}
