package com.example.beginnerfitbe.friend.repository;

import com.example.beginnerfitbe.friend.domain.Friend;
import com.example.beginnerfitbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findBySenderAndReceiver(User sender, User receiver);

}
