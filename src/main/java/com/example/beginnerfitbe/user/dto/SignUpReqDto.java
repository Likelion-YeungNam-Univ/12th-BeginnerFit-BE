package com.example.beginnerfitbe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpReqDto {
    private String email;
    private String name;
    private String password;
}
