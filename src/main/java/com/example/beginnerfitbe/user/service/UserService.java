package com.example.beginnerfitbe.user.service;


import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.s3.util.S3Uploader;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.dto.UserUpdateDto;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

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
    public ResponseEntity<StateResponse> update(Long id, UserUpdateDto requestDto, MultipartFile profilePicture) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
            String previousPictureUrl = user.getProfilePictureUrl();

            //정보 업데이트
            if (requestDto.getName() != null && requestDto.getPassword() != null
                    && requestDto.getExercisePurpose()!=0 && requestDto.getExercisePart()!=0
                    && requestDto.getExerciseTime()!=0 && requestDto.getExerciseIntensity()!=0 ) {
                user.update(requestDto.getName(), requestDto.getPassword(), requestDto.getExercisePurpose(), requestDto.getExercisePart(), requestDto.getExerciseTime(), requestDto.getExerciseIntensity());

            }
            if (previousPictureUrl != null) {
                s3Uploader.delete("User",previousPictureUrl);
            }
            //사진 업로드 or 변경
            String pictureUrl = null;
            if (profilePicture != null && !profilePicture.isEmpty()) {
                if (previousPictureUrl != null) {
                    s3Uploader.delete("User",previousPictureUrl);
                }
                pictureUrl = s3Uploader.upload(profilePicture, "User");
            }
            user.updatePicture(pictureUrl);
            userRepository.save(user);
            return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("정보를 성공적으로 업데이트했습니다.").build());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StateResponse.builder().code("ERROR").message("오류가 발생했습니다: " + e.getMessage()).build());
        }
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


