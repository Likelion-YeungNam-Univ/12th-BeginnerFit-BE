package com.example.beginnerfitbe.user.dto;

import com.example.beginnerfitbe.user.domain.User;
import lombok.Getter;

@Getter
public class SignUpResDto {
    private final String eamil;
    private final String password;

    public SignUpResDto(User user){
        this.eamil=user.getEmail();
        this.password=user.getPassword();
    }

}
