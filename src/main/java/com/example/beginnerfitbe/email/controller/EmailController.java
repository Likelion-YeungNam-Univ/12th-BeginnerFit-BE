package com.example.beginnerfitbe.email.controller;

import com.example.beginnerfitbe.email.dto.EmailDto;
import com.example.beginnerfitbe.email.service.EmailService;
import com.example.beginnerfitbe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class EmailController {
    private final EmailService emailService;
    private final UserService userService;
    @PostMapping("/email-send")
    public ResponseEntity<?> emailSend(@RequestParam String email) throws Exception {
        return ResponseEntity.ok(emailService.sendEmail(email));
    }
    @PostMapping("/email-verify")
    public ResponseEntity<?> codeVerification(@RequestBody EmailDto emailDto){
        return ResponseEntity.ok(emailService.codeVerification(emailDto));
    }

}
