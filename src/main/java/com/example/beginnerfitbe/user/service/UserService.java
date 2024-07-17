package com.example.beginnerfitbe.user.service;


import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.dto.UserUpdateDto;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public List<User> list(){
        return userRepository.findAll();
    }
    public void create(User user) {
        userRepository.save(user);
    }

    public User read(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    public User read(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    //회원정보 수정
    public ResponseEntity<StateResponse> update(Long id, UserUpdateDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (requestDto.getName() != null && requestDto.getPassword() != null
                && requestDto.getExercisePurpose()!=0 && requestDto.getExercisePart()!=0
                && requestDto.getExerciseTime()!=0 && requestDto.getExerciseIntensity()!=0 ) {
            user.update(requestDto.getName(), requestDto.getPassword(), requestDto.getExercisePurpose(), requestDto.getExercisePart(), requestDto.getExerciseTime(), requestDto.getExerciseIntensity());

        }
        userRepository.save(user);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("정보를 성공적으로 업데이트했습니다.").build());
    }

    //회원 탈퇴
    public ResponseEntity<StateResponse> withdrawal(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("성공적으로 회원탈퇴 처리되었습니다.").build());
    }

    public Boolean emailCheck(String email) {
        Optional<User> checkUserEmail = userRepository.findByEmail(email);
        return checkUserEmail.isPresent();
    }


}


