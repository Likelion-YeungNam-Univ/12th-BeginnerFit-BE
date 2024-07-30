package com.example.beginnerfitbe.challengeparticipant.controller;

import com.example.beginnerfitbe.challengeparticipant.dto.ChallengeDateDTO;
import com.example.beginnerfitbe.challengeparticipant.dto.ChallengeParticipantDTO;
import com.example.beginnerfitbe.challengeparticipant.service.ChallengeParticipantService;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challengeparticipant")
@RequiredArgsConstructor
public class ChallengeParticipantController {

    private final ChallengeParticipantService challengeParticipantService;
    private final JwtUtil jwtUtil;

    @GetMapping("/today-challenges") // 현재 로그인한 유저의 챌린지 참가 정보 조회
    public ResponseEntity<List<ChallengeParticipantDTO>> getMyChallenges(HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        List<ChallengeParticipantDTO> challengeParticipants = challengeParticipantService.getUserChallenges(userId);
        return ResponseEntity.ok(challengeParticipants);
    }

    @PutMapping("/complete/{challengeId}") // 챌린지 완료 처리
    public ResponseEntity<Void> completeChallenge(HttpServletRequest request, @PathVariable Long challengeId ) {

        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        challengeParticipantService.completeChallenge(userId, challengeId);
        return ResponseEntity.noContent().build(); // 204 No Content 응답
    }

    @PutMapping("/notcomplete/{challengeId}") // 챌린지 실패 처리
    public ResponseEntity<Void> notcompleteChallenge(HttpServletRequest request, @PathVariable Long challengeId ) {

        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        challengeParticipantService.notcompleteChallenge(userId, challengeId);
        return ResponseEntity.noContent().build(); // 204 No Content 응답
    }

    @GetMapping("/completed-month-challenges") // 특정 년도, 월의 완료된 챌린지 조회
    public ResponseEntity<List<ChallengeParticipantDTO>> getCompletedChallenges(HttpServletRequest request, @RequestBody ChallengeDateDTO challengeDateDTO) {

        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        List<ChallengeParticipantDTO> completedChallenges = challengeParticipantService.getCompletedChallengesByDate(
                userId, challengeDateDTO.getYear(), challengeDateDTO.getMonth());

        return ResponseEntity.ok(completedChallenges);
    }



}
