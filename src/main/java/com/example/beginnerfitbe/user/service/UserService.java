package com.example.beginnerfitbe.user.service;


import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.playlist.service.PlaylistService;
import com.example.beginnerfitbe.s3.util.S3Uploader;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.dto.HealthInfoReqDto;
import com.example.beginnerfitbe.user.dto.UserDto;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;
    private final PlaylistService playlistService;

    public List<UserDto> list(){
        return userRepository.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }
    public void create(User user) throws IOException {
        userRepository.save(user);

    }

    public UserDto read(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserDto.fromEntity(user);
    }
    public UserDto readByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserDto.fromEntity(user);
    }
    public List<UserDto> me(Long id){
        return userRepository.findById(id).stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    //건강정보 업데이트
    public StateResponse updateHealthInfo(HealthInfoReqDto dto) throws IOException {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateHealthInfo(
                dto.getHeight(),
                dto.getWeight(),
                dto.getTargetWeight(),
                dto.getDate(),
                dto.getTargetDate(),
                dto.getExerciseTime(),
                dto.getExerciseIntensity(),
                dto.getExerciseGoals(),
                dto.getConcernedAreas()
        );

        userRepository.save(user);

        //플레이리스트 생성
        playlistService.create(user);

        return StateResponse.builder()
                .code("SUCCESS")
                .message("건강정보가 업데이트 되었습니다.")
                .build();
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


