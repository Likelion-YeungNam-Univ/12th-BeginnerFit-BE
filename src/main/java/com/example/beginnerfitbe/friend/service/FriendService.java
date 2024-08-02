package com.example.beginnerfitbe.friend.service;

import com.example.beginnerfitbe.alarm.domain.AlarmType;
import com.example.beginnerfitbe.alarm.service.AlarmService;
import com.example.beginnerfitbe.friend.domain.Friend;
import com.example.beginnerfitbe.friend.dto.FriendDTO;
import com.example.beginnerfitbe.friend.repository.FriendRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.dto.OtherUserDto;
import com.example.beginnerfitbe.user.dto.UserResponseDto;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final AlarmService alarmService;

    @Transactional
    public FriendDTO sendPathRequest(Long senderId, Long receiverId) {

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender user not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("요청을 보낼 사용자가 존재하지 않습니다."));

        // 이미 친구인지 확인
        Optional<Friend> existingFriend = friendRepository.findBySenderIdAndReceiverId(senderId, receiver.getId());
        Optional<Friend> reexistingFriend = friendRepository.findBySenderIdAndReceiverId( receiver.getId(), senderId);
        if (existingFriend.isPresent() && existingFriend.get().isAccepted()) {
            throw new RuntimeException("이미 친구입니다.");
        }

        else if (existingFriend.isPresent() ) {
            // 친구 요청이 이미 존재하지만 승인되지 않은 경우
            throw new RuntimeException("친구 요청 목록을 확인해주세요.");
        }

        else if (reexistingFriend.isPresent()) {
            if(reexistingFriend.get().isAccepted()){
                throw new RuntimeException("이미 친구입니다.");
            }
            throw new RuntimeException("이미 친구 요청이 온 사용자입니다. 친구 요청을 수락해주세요.");
        }

        else {
            Friend friend = Friend.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .isAccepted(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            Friend savedFriend = friendRepository.save(friend);

            String alarmMessage = sender.getName();
            alarmService.createAlarm(receiver, alarmMessage, AlarmType.FRIEND_REQUEST);

            return FriendDTO.fromEntity(savedFriend);
        }
    }


    public FriendDTO sendFriendRequest(Long senderId, String receiverEmail) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender user not found"));
        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new RuntimeException("요청을 보낼 사용자가 존재하지 않습니다."));

        // 이미 친구인지 확인
        Optional<Friend> existingFriend = friendRepository.findBySenderIdAndReceiverId(senderId, receiver.getId());
        Optional<Friend> reexistingFriend = friendRepository.findBySenderIdAndReceiverId( receiver.getId(), senderId);
        if (existingFriend.isPresent() && existingFriend.get().isAccepted()) {
            throw new RuntimeException("이미 친구입니다.");
        }

        else if (existingFriend.isPresent() ) {
            // 친구 요청이 이미 존재하지만 승인되지 않은 경우
            throw new RuntimeException("친구 요청 목록을 확인해주세요.");
        }

        else if (reexistingFriend.isPresent()) {
            if(reexistingFriend.get().isAccepted()){
                throw new RuntimeException("이미 친구입니다.");
            }
            throw new RuntimeException("이미 친구 요청이 온 사용자입니다. 친구 요청을 수락해주세요.");
        }


        else {
            Friend friend = Friend.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .isAccepted(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            Friend savedFriend = friendRepository.save(friend);

            String alarmMessage = sender.getName();
            alarmService.createAlarm(receiver, alarmMessage, AlarmType.FRIEND_REQUEST);

            return FriendDTO.fromEntity(savedFriend);
        }

    }

    public List<OtherUserDto> getPendingFriendRequests(Long userId) {
        List<Friend> pendingFriendRequests = friendRepository.findByReceiverIdAndIsAcceptedFalse(userId);

        return pendingFriendRequests.stream()
                .map(friend -> {
                    User sender = friend.getSender();
                    return new OtherUserDto(
                            sender.getId(),
                            sender.getEmail(),
                            sender.getName(),
                            sender.getHeight(),
                            sender.getWeight(),
                            sender.getTargetWeight(),
                            sender.getDate(),
                            sender.getTargetDate(),
                            sender.getExerciseTime(),
                            sender.getExerciseGoals(),
                            sender.getConcernedAreas(),
                            sender.getExerciseIntensity(),
                            sender.getProfileUrl()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<OtherUserDto> getFriendWaitingService(Long userId) {
        List<Friend> FriendWaitingService = friendRepository.findBySenderIdAndIsAcceptedFalse(userId);

        return FriendWaitingService.stream()
                .map(friend -> {
                    User receiver = friend.getReceiver();
                    return new OtherUserDto(
                            receiver.getId(),
                            receiver.getEmail(),
                            receiver.getName(),
                            receiver.getHeight(),
                            receiver.getWeight(),
                            receiver.getTargetWeight(),
                            receiver.getDate(),
                            receiver.getTargetDate(),
                            receiver.getExerciseTime(),
                            receiver.getExerciseGoals(),
                            receiver.getConcernedAreas(),
                            receiver.getExerciseIntensity(),
                            receiver.getProfileUrl()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<OtherUserDto> getAcceptedFriendRequests(Long userId) {
        // 수신자로서 수락된 친구 요청 목록을 가져옵니다.
        List<Friend> acceptedFriendRequests = friendRepository.findByReceiverIdAndIsAcceptedTrue(userId);
        List<Long> receiverIds = friendRepository.findReceiverIdsBySenderId(userId);
        List<User> receivers = userRepository.findAllById(receiverIds);

        List<OtherUserDto> acceptedRequestsDtos = acceptedFriendRequests.stream()
                .map(friend -> {
                    User sender = friend.getSender();
                    return new OtherUserDto(
                            sender.getId(),
                            sender.getEmail(),
                            sender.getName(),
                            sender.getHeight(),
                            sender.getWeight(),
                            sender.getTargetWeight(),
                            sender.getDate(),
                            sender.getTargetDate(),
                            sender.getExerciseTime(),
                            sender.getExerciseGoals(),
                            sender.getConcernedAreas(),
                            sender.getExerciseIntensity(),
                            sender.getProfileUrl()
                    );
                })
                .collect(Collectors.toList());

        for (User receiver : receivers) {
            acceptedRequestsDtos.add(new OtherUserDto(
                    receiver.getId(),
                    receiver.getEmail(),
                    receiver.getName(),
                    receiver.getHeight(),
                    receiver.getWeight(),
                    receiver.getTargetWeight(),
                    receiver.getDate(),
                    receiver.getTargetDate(),
                    receiver.getExerciseTime(),
                    receiver.getExerciseGoals(),
                    receiver.getConcernedAreas(),
                    receiver.getExerciseIntensity(),
                    receiver.getProfileUrl()
            ));
        }

        return acceptedRequestsDtos;
    }

    public void acceptFriendRequest(Long senderId, Long receiverId) {
        Friend friend = friendRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        friend.accept();
        friendRepository.save(friend);

        // 알림 메시지 생성
        String alarmMessage =friend.getReceiver().getName();

        // 알림 생성
        alarmService.createAlarm(friend.getSender(), alarmMessage, AlarmType.FRIEND_ACCEPTANCE);
    }

    public void rejectFriendRequest(Long senderId, Long receiverId) {
        Friend friend = friendRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        friendRepository.delete(friend);
    }

    public OtherUserDto getFriendInfo(Long senderId, String receiverEmail) {
        // 이메일로 User 찾기
        User friend = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new RuntimeException("해당 친구가 존재하지 않습니다."));

        // 친구 목록에 있는지 확인
        boolean isFriend = friendRepository.existsByReceiverIdAndSenderIdAndIsAcceptedTrue(senderId, friend.getId())
                || friendRepository.existsByReceiverIdAndSenderIdAndIsAcceptedTrue(friend.getId(), senderId);

        if (!isFriend) {
            throw new RuntimeException("해당 친구가 존재하지 않습니다.");
        }

        // OtherUserDto로 변환
        return new OtherUserDto(
                friend.getId(),
                friend.getEmail(),
                friend.getName(),
                friend.getHeight(),
                friend.getWeight(),
                friend.getTargetWeight(),
                friend.getDate(),
                friend.getTargetDate(),
                friend.getExerciseTime(),
                friend.getExerciseGoals(),
                friend.getConcernedAreas(),
                friend.getExerciseIntensity(),
                friend.getProfileUrl()
        );
    }
    @Transactional(readOnly = true)
    public List<UserResponseDto> getNonFriendUsers(Long userId, String searchName) {
        // 모든 사용자 조회
        List<User> allUsers = userRepository.findAll();

        // 친구 목록 조회
        List<Friend> friends = friendRepository.findBySenderIdOrReceiverId(userId, userId);

        // 친구가 아닌 사용자 필터링
        List<Long> friendIds = friends.stream()
                .filter(Friend::isAccepted)
                .map(friend -> friend.getSender().getId() == userId ? friend.getReceiver().getId() : friend.getSender().getId())
                .collect(Collectors.toList());

         //친구 요청을 보낸 사용자 목록 추가
        List<Long> pendingFriendRequests = friends.stream()
                .filter(friend -> !friend.isAccepted() && friend.getSender().getId().equals(userId))
                .map(friend -> friend.getReceiver().getId())
                .collect(Collectors.toList());

        // 친구 요청을 받은 사용자 목록 추가
        List<Long> receivedFriendRequests = friends.stream()
                .filter(friend -> !friend.isAccepted() && friend.getReceiver().getId().equals(userId))
                .map(friend -> friend.getSender().getId())
                .collect(Collectors.toList());

        // 친구가 아닌 사용자 목록 반환
        return allUsers.stream()
                .filter(user -> !friendIds.contains(user.getId()) // 친구 목록에 없는 사용자
                        && !pendingFriendRequests.contains(user.getId()) // 친구 요청을 보낸 사용자
                        && !receivedFriendRequests.contains(user.getId()) // 친구 요청을 받은 사용자
                        && !user.getId().equals(userId)) // 자신 제외
                .filter(user -> user.getName().contains(searchName)) // 이름에 searchName이 포함된 사용자 필터링
                .map(user -> new UserResponseDto(user.getId(), user.getEmail(), user.getName(), user.getProfileUrl()))
                .collect(Collectors.toList());
    }


}
