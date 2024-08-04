package com.example.beginnerfitbe.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignInResDto {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}

