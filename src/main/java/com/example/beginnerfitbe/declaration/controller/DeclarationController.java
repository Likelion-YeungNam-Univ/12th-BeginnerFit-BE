package com.example.beginnerfitbe.declaration.controller;

import com.example.beginnerfitbe.declaration.dto.DeclarationDto;
import com.example.beginnerfitbe.declaration.dto.DeclarationReqDto;
import com.example.beginnerfitbe.declaration.service.DeclarationService;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class DeclarationController {

    private final DeclarationService declarationService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{postId}/reports")
    @Operation(summary = "게시글 신고 메서드", description = "게시글을 신고합니다.")
    public ResponseEntity<String> create(HttpServletRequest request, @PathVariable Long postId,  @RequestBody DeclarationReqDto declarationReqDto){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        DeclarationDto declarationDto = declarationService.create(userId, postId, declarationReqDto);
        if(declarationDto!=null){
            return ResponseEntity.ok("댓글이 성공적으로 신고되었습니다!");
        }
        else{
            return ResponseEntity.badRequest().body("댓글 신고에 실패했습니다.");
        }
    }

}
