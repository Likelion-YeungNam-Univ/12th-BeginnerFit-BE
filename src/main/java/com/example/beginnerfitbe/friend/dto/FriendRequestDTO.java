package com.example.beginnerfitbe.friend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestDTO {
    private String receiverEmail;
}