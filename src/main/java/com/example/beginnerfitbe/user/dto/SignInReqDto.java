package com.example.beginnerfitbe.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignInReqDto {
    private String email;
    private String password;
}
