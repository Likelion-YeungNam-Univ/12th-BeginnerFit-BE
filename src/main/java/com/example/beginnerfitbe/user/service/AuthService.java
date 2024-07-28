package com.example.beginnerfitbe.user.service;

import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.dto.SignInReqDto;
import com.example.beginnerfitbe.user.dto.SignInResDto;
import com.example.beginnerfitbe.user.dto.SignUpReqDto;
import com.example.beginnerfitbe.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public User signUp (SignUpReqDto dto){
        String email=dto.getEmail();
        String name=dto.getName();
        String password=passwordEncoder.encode(dto.getPassword());

        //중복 닉네임 확인
        if(userService.nameCheck(name)){
            throw new IllegalArgumentException("이미 등록된 닉네임입니다.");
        }
        //회원 기본 정보만 입력
        return User.builder()
                .email(email)
                .name(name)
                .password(password)
                .profileUrl(null)
                .height(0)
                .weight(0)
                .targetWeight(0)
                .date(null)
                .targetDate(null)
                .exerciseTime(0)
                .exerciseIntensity(new ArrayList<>())
                .concernedAreas(new ArrayList<>())
                .exerciseGoals(new ArrayList<>())
                .build();

    }
    public SignInResDto signIn(SignInReqDto dto) {
        UserDto userDto = userService.readByEmail(dto.getEmail());
        if (passwordEncoder.matches(dto.getPassword(), userDto.getPassword())) {
            return new SignInResDto(jwtUtil.generateToken(userDto.getEmail(), userDto.getId()));
        } else {
            throw new IllegalArgumentException("Invalid password");
        }
    }

}