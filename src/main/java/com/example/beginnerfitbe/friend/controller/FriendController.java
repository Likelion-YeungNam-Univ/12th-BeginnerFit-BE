package com.example.beginnerfitbe.friend.controller;

import com.example.beginnerfitbe.friend.dto.FriendDTO;
import com.example.beginnerfitbe.friend.dto.FriendRequestDTO;
import com.example.beginnerfitbe.friend.service.FriendService;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.user.dto.OtherUserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<OtherUserDto>> getPendingFriendRequests(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        List<OtherUserDto> pendingFriendRequests = friendService.getPendingFriendRequests(userId);
        return ResponseEntity.ok(pendingFriendRequests);
    }

    @PostMapping("/accept/{senderId}")
    public ResponseEntity<Void> acceptFriendRequest(HttpServletRequest request, @PathVariable Long senderId) {
        String token = jwtUtil.resolveToken(request);
        Long receiverId = jwtUtil.getUserId(token.substring(7));


        friendService.acceptFriendRequest(senderId, receiverId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject/{senderId}")
    public ResponseEntity<Void> rejectFriendRequest(HttpServletRequest request, @PathVariable Long senderId) {
        String token = jwtUtil.resolveToken(request);
        Long receiverId = jwtUtil.getUserId(token.substring(7));


        friendService.rejectFriendRequest(senderId, receiverId);
        return ResponseEntity.ok().build();
    }
}
