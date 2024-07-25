package com.example.beginnerfitbe.declaration.repository;

import com.example.beginnerfitbe.declaration.domain.Declaration;
import com.example.beginnerfitbe.declaration.dto.DeclarationDto;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {
    Optional<Declaration> findByUserAndPost(User user, Post post);
    List<Declaration> findByPost(Post post);
}
