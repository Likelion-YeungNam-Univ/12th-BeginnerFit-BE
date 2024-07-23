package com.example.beginnerfitbe.friend.repository;

import com.example.beginnerfitbe.friend.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findByReceiverIdAndIsAcceptedFalse(Long receiverId);
    Optional<Friend> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    List<Friend> findByReceiverIdAndIsAcceptedTrue(Long receiverId);

    @Query("SELECT f.receiver.id FROM Friend f WHERE f.sender.id = :senderId AND f.isAccepted = true")
    List<Long> findReceiverIdsBySenderId(Long senderId);
    List<Friend> findBySenderIdAndIsAcceptedFalse(Long senderId);

}
