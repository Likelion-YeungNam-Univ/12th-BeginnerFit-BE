package com.example.beginnerfitbe.friend.dto;

import com.example.beginnerfitbe.friend.domain.Friend;
import lombok.*;

import java.time.LocalDateTime;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendDTO {
    private Long friendId;
    private Long senderId;
    private Long receiverId;
    private boolean isAccepted;
    private LocalDateTime createdAt;

    public static FriendDTO fromEntity(Friend friend) {
        return FriendDTO.builder()
                .friendId(friend.getFriendId())
                .senderId(friend.getSender().getId())
                .receiverId(friend.getReceiver().getId())
                .isAccepted(friend.isAccepted())
                .createdAt(friend.getCreatedAt())
                .build();
    }
}