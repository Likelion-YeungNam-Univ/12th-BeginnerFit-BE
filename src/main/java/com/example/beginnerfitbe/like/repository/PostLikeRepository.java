package com.example.beginnerfitbe.like.repository;

import com.example.beginnerfitbe.like.domain.PostLike;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
    List<PostLike> findByUser(User user);
    List<PostLike> findByPost(Post post);
}
