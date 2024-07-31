package com.example.beginnerfitbe.friend.controller;

import com.example.beginnerfitbe.friend.dto.FriendDTO;
import com.example.beginnerfitbe.friend.dto.FriendRequestDTO;
import com.example.beginnerfitbe.friend.dto.FriendSearchDTO;
import com.example.beginnerfitbe.friend.service.FriendService;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.user.dto.OtherUserDto;
import com.example.beginnerfitbe.user.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping("/request/{userId}")
    public ResponseEntity<String> sendPathRequest(@PathVariable Long userId, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long currentUserId = jwtUtil.getUserId(token.substring(7));

        friendService.sendPathRequest(currentUserId, userId);
        return ResponseEntity.ok("Friend request sent.");
    }

    @PostMapping("/request")
    @Operation(summary = "친구 요청 메서드", description = "친구를 맺고싶은 유저의 이메일을 입력하여 친구 요청을 합니다.")
    public ResponseEntity<FriendDTO> sendFriendRequest(HttpServletRequest request, @RequestBody FriendRequestDTO friendRequestDTO) {
        String token = jwtUtil.resolveToken(request);
        Long senderId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        FriendDTO friendDTO = friendService.sendFriendRequest(senderId, friendRequestDTO.getReceiverEmail());
        return ResponseEntity.ok(friendDTO);
    }

    @GetMapping("/pending")
    @Operation(summary = "친구 요청 대기 목록 조회 메서드", description = "나에게 친구 요청이 온 사용자들의 목록을 조회합니다.")
    public ResponseEntity<List<OtherUserDto>> getPendingFriendRequests(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        List<OtherUserDto> pendingFriendRequests = friendService.getPendingFriendRequests(userId);
        return ResponseEntity.ok(pendingFriendRequests);
    }

    @GetMapping("/waiting")
    @Operation(summary = "요청중인 친구 목록 조회 메서드", description = "내가 보낸 친구 요청 중 아직 수락되지 않은 목록을 조회합니다.")
    public ResponseEntity<List<OtherUserDto>> getFriendWaitingService(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        List<OtherUserDto> friendWaitingService = friendService.getFriendWaitingService(userId);
        return ResponseEntity.ok(friendWaitingService);
    }

    @GetMapping("")
    @Operation(summary = "친구 목록 조회 메서드", description = "친구로 맺어진 사용자들의 목록을 조회합니다.")
    public ResponseEntity<List<OtherUserDto>> getAcceptedFriendRequests(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        List<OtherUserDto> acceptedFriendRequests = friendService.getAcceptedFriendRequests(userId);
        return ResponseEntity.ok(acceptedFriendRequests);
    }

    @PutMapping("/accept/{senderId}")
    @Operation(summary = "친구 수락 메서드", description = "친구 요청을 수락합니다.")
    public ResponseEntity<Void> acceptFriendRequest(HttpServletRequest request, @PathVariable Long senderId) {
        String token = jwtUtil.resolveToken(request);
        Long receiverId = jwtUtil.getUserId(token.substring(7));


        friendService.acceptFriendRequest(senderId, receiverId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{senderId}")
    @Operation(summary = "친구 삭제 및 친구 거절 메서드", description = "친구를 삭제 및 친구요청을 거절합니다.")
    public ResponseEntity<Void> rejectFriendRequest(HttpServletRequest request, @PathVariable Long senderId) {
        String token = jwtUtil.resolveToken(request);
        Long receiverId = jwtUtil.getUserId(token.substring(7));


        friendService.rejectFriendRequest(senderId, receiverId);
        return ResponseEntity.ok().build();
    }

    @GetMapping ("/search")
    @Operation(summary = "친구 검색 메서드", description = "친구목록 리스트에서 이메일을 입력하여 원하는 사용자를 찾습니다.")
    public ResponseEntity<OtherUserDto> getFriendInfo(HttpServletRequest request, @RequestBody FriendRequestDTO friendRequestDTO) {
        String token = jwtUtil.resolveToken(request);
        Long senderId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));

        OtherUserDto friendInfo  = friendService.getFriendInfo(senderId, friendRequestDTO.getReceiverEmail());
        return ResponseEntity.ok(friendInfo );
    }

    @PostMapping("/non-friends")
    @Operation(summary = "친구 등록을 위한 목록 조회 메서드", description = "사용자 목록 리스트에서 친구로 등록되어 있지 않은 사용자들의 목록을 반환합니다.(본인이 요청을 보낸 상대는 목록 조회에서 제외됨)")
    public List<UserResponseDto> getNonFriendUsers(HttpServletRequest request, @RequestBody FriendSearchDTO friendSearchDTO) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        return friendService.getNonFriendUsers(userId, friendSearchDTO.getSearchName());
    }


}
