package com.example.beginnerfitbe.friend.service;

import com.example.beginnerfitbe.friend.domain.Friend;
import com.example.beginnerfitbe.friend.dto.FriendDTO;
import com.example.beginnerfitbe.friend.repository.FriendRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.dto.OtherUserDto;
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
                            sender.getExercisePurpose(),
                            sender.getExercisePart(),
                            sender.getExerciseTime(),
                            sender.getExerciseIntensity()
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
                            receiver.getExercisePurpose(),
                            receiver.getExercisePart(),
                            receiver.getExerciseTime(),
                            receiver.getExerciseIntensity()
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
                            sender.getExercisePurpose(),
                            sender.getExercisePart(),
                            sender.getExerciseTime(),
                            sender.getExerciseIntensity()
                    );
                })
                .collect(Collectors.toList());

        for (User receiver : receivers) {
            acceptedRequestsDtos.add(new OtherUserDto(
                    receiver.getId(),
                    receiver.getEmail(),
                    receiver.getName(),
                    receiver.getExercisePurpose(),
                    receiver.getExercisePart(),
                    receiver.getExerciseTime(),
                    receiver.getExerciseIntensity()
            ));
        }

        return acceptedRequestsDtos;
    }

    public void acceptFriendRequest(Long senderId, Long receiverId) {
        Friend friend = friendRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        friend.accept();
        friendRepository.save(friend);
    }

    public void rejectFriendRequest(Long senderId, Long receiverId) {
        Friend friend = friendRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        friendRepository.delete(friend);
    }

}
