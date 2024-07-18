package com.example.beginnerfitbe.user.service;

import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.dto.SignInReqDto;
import com.example.beginnerfitbe.user.dto.SignInResDto;
import com.example.beginnerfitbe.user.dto.SignUpReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        int exercisePurpose = dto.getExercisePurpose();
        int exercisePart =dto.getExercisePart();
        int exerciseTime = dto.getExerciseTime();
        int exerciseIntensity = dto.getExerciseIntensity();

        //중복 가입 확인
        if(userService.emailCheck(email)){
            throw new IllegalArgumentException("이미 등록된 사용자입니다.");
        }
        User user = User.builder()
                .email(email)
                .name(name)
                .password(password)
                .exercisePurpose(exercisePurpose)
                .exercisePart(exercisePart)
                .exerciseTime(exerciseTime)
                .exerciseIntensity(exerciseIntensity)
                .build();

        return user;
    }
    public SignInResDto signIn(SignInReqDto dto) {
        User user = userService.read(dto.getEmail());
        if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return new SignInResDto(jwtUtil.generateToken(user.getEmail(), user.getId()));
        } else {
            throw new IllegalArgumentException("Invalid password");
        }
    }

}