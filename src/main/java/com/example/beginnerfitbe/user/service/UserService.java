package com.example.beginnerfitbe.user.service;


import com.example.beginnerfitbe.comment.repository.CommentRepository;
import com.example.beginnerfitbe.declaration.repository.DeclarationRepository;
import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.like.repository.PostLikeRepository;
import com.example.beginnerfitbe.playlist.service.PlaylistService;
import com.example.beginnerfitbe.post.service.PostService;
import com.example.beginnerfitbe.scrap.repository.ScrapRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.dto.HealthInfoReqDto;
import com.example.beginnerfitbe.user.dto.UserDto;
import com.example.beginnerfitbe.user.dto.UserUpdateDto;
import com.example.beginnerfitbe.user.repository.UserRepository;
import com.example.beginnerfitbe.weight.service.WeightRecordService;
import jakarta.transaction.Transactional;
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
    private final PlaylistService playlistService;
    private final WeightRecordService weightRecordService;
    private final PostService postService;

    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final ScrapRepository scrapRepository;
    private final DeclarationRepository declarationRepository;

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
    public String getEmailByName(String name){
        User user = userRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getEmail();
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

        //몸무게 기록
        weightRecordService.create(user.getId(), user.getWeight());

        return StateResponse.builder()
                .code("SUCCESS")
                .message("건강정보가 업데이트 되었습니다.")
                .build();
    }

    public StateResponse update(Long userId, UserUpdateDto dto){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.updateName(dto.getName());

        userRepository.save(user);

        return StateResponse.builder()
                .code("SUCCESS")
                .message("사용자 이름이 업데이트 되었습니다.")
                .build();

    }
    public StateResponse resetPassword(String email,String password){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        user. updatePassword(password);
        userRepository.save(user);

        return StateResponse.builder()
                .code("SUCCESS")
                .message("비밀번호가 재발급 되었습니다.")
                .build();

    }
    @Transactional
    //회원 탈퇴
    public ResponseEntity<StateResponse> withdrawal(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));


        scrapRepository.deleteAllByUserId(id);
        declarationRepository.deleteAllByUserId(id);
        postLikeRepository.deleteAllByUserId(id);
        commentRepository.deleteAllByUserId(id);

        postService.deleteByUserId(id);
        playlistService.deleteByUserId(id);

        userRepository.delete(user);

        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("성공적으로 회원탈퇴 처리되었습니다.").build());
    }

    //이름 중복 x
    public Boolean nameCheck(String name) {
        Optional<User> checkUserName = userRepository.findByName(name);
        return checkUserName.isPresent();
    }

    //이름 중복 x
    public Boolean emailCheck(String email) {
        Optional<User> checkEmail = userRepository.findByEmail(email);
        return checkEmail.isPresent();
    }



}


