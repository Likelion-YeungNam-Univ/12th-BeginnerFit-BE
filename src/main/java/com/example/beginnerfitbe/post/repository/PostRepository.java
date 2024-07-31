package com.example.beginnerfitbe.post.repository;

import com.example.beginnerfitbe.category.domain.Category;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUser(User user);
    List<Post> findAllByUserId(Long userId);
    List<Post> findPostsByCategory(Category category);

    @Transactional
    @Modifying
    @Query("DELETE FROM Post p WHERE p.user.id = :userId")
    void deleteAllByUserId(Long userId);
}
