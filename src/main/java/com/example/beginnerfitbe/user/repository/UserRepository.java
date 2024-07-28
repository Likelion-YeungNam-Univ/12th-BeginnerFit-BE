package com.example.beginnerfitbe.user.repository;

import com.example.beginnerfitbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //중복 가입 확인
    Optional<User> findByEmail(String id);
    Optional<User> findByName(String name);
}

