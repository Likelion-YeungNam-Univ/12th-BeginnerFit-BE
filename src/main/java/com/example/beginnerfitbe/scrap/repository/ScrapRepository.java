package com.example.beginnerfitbe.scrap.repository;

import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.scrap.domain.Scrap;
import com.example.beginnerfitbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByUserAndPost(User user, Post post);
    List<Scrap> findScrapsByUser(User user);
    List<Scrap> findScrapsByPost(Post post);
}
