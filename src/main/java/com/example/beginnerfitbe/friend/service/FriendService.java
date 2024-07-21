package com.example.beginnerfitbe.friend.service;

import com.example.beginnerfitbe.friend.domain.Friend;
import com.example.beginnerfitbe.friend.dto.FriendDTO;
import com.example.beginnerfitbe.friend.repository.FriendRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;


    public FriendDTO sendFriendRequest(Long senderId, String receiverEmail) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender user not found"));
        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new RuntimeException("Receiver user not found"));

        Friend friend = Friend.builder()
                .sender(sender)
                .receiver(receiver)
                .isAccepted(false)
                .createdAt(LocalDateTime.now())
                .build();

        Friend savedFriend = friendRepository.save(friend);
        return FriendDTO.fromEntity(savedFriend);
    }

}
