package com.example.beginnerfitbe.friend.controller;

import com.example.beginnerfitbe.friend.dto.FriendDTO;
import com.example.beginnerfitbe.friend.dto.FriendRequestDTO;
import com.example.beginnerfitbe.friend.service.FriendService;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final JwtUtil jwtUtil;

    @PostMapping("/request")
    public ResponseEntity<FriendDTO> sendFriendRequest(HttpServletRequest request, @RequestBody FriendRequestDTO friendRequestDTO) {
        String token = jwtUtil.resolveToken(request);
        System.out.println("Resolved Token: " + token);

        Long senderId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        System.out.println("Sender ID: " + senderId);
        FriendDTO friendDTO = friendService.sendFriendRequest(senderId, friendRequestDTO.getReceiverEmail());
        return ResponseEntity.ok(friendDTO);
    }
}
