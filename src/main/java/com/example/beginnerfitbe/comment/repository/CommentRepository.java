package com.example.beginnerfitbe.comment.repository;

import com.example.beginnerfitbe.comment.domain.Comment;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByUser(User user);
    List<Comment> findCommentsByPost(Post post);
}
