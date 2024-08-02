package com.example.beginnerfitbe.challengeparticipant.service;

import com.example.beginnerfitbe.alarm.domain.AlarmType;
import com.example.beginnerfitbe.alarm.service.AlarmService;
import com.example.beginnerfitbe.challengeparticipant.domain.ChallengeParticipant;
import com.example.beginnerfitbe.challengeparticipant.dto.ChallengeParticipantDTO;
import com.example.beginnerfitbe.challengeparticipant.dto.ChallengeRankingDto;
import com.example.beginnerfitbe.challengeparticipant.repository.ChallengeParticipantRepository;
import com.example.beginnerfitbe.friend.domain.Friend;
import com.example.beginnerfitbe.friend.repository.FriendRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.dto.OtherUserDto;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeParticipantService {

    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final AlarmService alarmService;

    public List<OtherUserDto> getAcceptedFriends(Long userId) {
        // 수락된 친구 목록 가져오기
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

    public long countCompletedFriends(List<Long> friendIds) {
        LocalDate today = LocalDate.now();

        // 친구 목록에서 오늘 날짜에 해당하는 모든 ChallengeParticipant가 isCompleted가 true인 친구의 수 카운트
        return friendIds.stream()
                .filter(friendId -> {
                    // 해당 친구의 오늘 날짜에 해당하는 ChallengeParticipant 목록을 가져옴
                    List<ChallengeParticipant> participants = challengeParticipantRepository.findByUserIdAndChallengeCompletedDate(friendId, today);

                    // 모든 ChallengeParticipant의 isCompleted가 true인지 확인
                    return participants.stream().allMatch(ChallengeParticipant::isCompleted);
                })
                .count();
    }

    @Transactional(readOnly = true)
    public List<ChallengeParticipantDTO> getUserChallenges(Long userId) {

        // 현재 날짜
        LocalDate currentDate = LocalDate.now();

        // userId와 현재 날짜로 ChallengeParticipant 리스트 반환
        List<ChallengeParticipant> participants = challengeParticipantRepository.findByUserIdAndChallengeCompletedDate(userId, currentDate);

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

        List<Long> friendIds = acceptedRequestsDtos.stream()
                .map(OtherUserDto::getId)
                .collect(Collectors.toList());

        // ID 리스트 출력
        System.out.println("Friend IDs: " + friendIds);

        // 친구들의 성공 카운트를 위한 결과 리스트
        List<ChallengeParticipantDTO> challengeParticipantDTOs = new ArrayList<>();

        for (ChallengeParticipant participant : participants) {
            Long challengeId = participant.getChallenge().getChallengeId();

            // 해당 챌린지에 대해 친구들의 성공 카운트
            long successCount = challengeParticipantRepository.countByChallengeIdAndUserIdIn(challengeId, friendIds);

            // DTO 변환 후 성공 카운트 설정
            ChallengeParticipantDTO dto = ChallengeParticipantDTO.fromEntity(participant);
            dto.setSuccessCount(successCount); // 성공 카운트 추가
            challengeParticipantDTOs.add(dto);
        }

        return challengeParticipantDTOs;
    }

    public void completeChallenge(Long userId, Long challengeId) {
        LocalDate currentDate = LocalDate.now();

        // 오늘 날짜이면서 주어진 challengeId와 userId에 해당하는 참가자 찾기
        ChallengeParticipant participant = challengeParticipantRepository
                .findByUserIdAndChallenge_ChallengeIdAndChallengeCompletedDate(userId, challengeId, currentDate)
                .orElseThrow(() -> new RuntimeException("Challenge participant not found")); // 예외 처리


        participant.setCompleted(true); // isCompleted를 true로 변경
        challengeParticipantRepository.save(participant); // 변경사항 저장


        // 해당 사용자의 오늘 날짜에 해당하는 챌린지 참가자 목록 가져오기 (challengeId 제거)
        List<ChallengeParticipant> participants = challengeParticipantRepository
                .findByUserIdAndChallengeCompletedDate(userId, currentDate);

        // isCompleted가 true인 참가자의 수를 세기
        long completedCount = participants.stream()
                .filter(ChallengeParticipant::isCompleted)
                .count();

        // completedCount가 2일 경우 알림 생성
        if (completedCount == 2) {
            String alarmMessage = "";
            // 알림 생성
            alarmService.createAlarm(participant.getUser(), alarmMessage, AlarmType.CHALLENGE_REMINDER);
        }




    }

    public void notcompleteChallenge(Long userId, Long challengeId) {
        LocalDate currentDate = LocalDate.now();

        // 오늘 날짜이면서 주어진 challengeId와 userId에 해당하는 참가자 찾기
        ChallengeParticipant participant = challengeParticipantRepository
                .findByUserIdAndChallenge_ChallengeIdAndChallengeCompletedDate(userId, challengeId, currentDate)
                .orElseThrow(() -> new RuntimeException("Challenge participant not found")); // 예외 처리

        // isCompleted를 true로 변경
        participant.setCompleted(false); // setter 메서드 필요
        challengeParticipantRepository.save(participant); // 변경사항 저장
    }

    public List<ChallengeParticipantDTO> getCompletedChallengesByDate(Long userId, int year, int month) {
        List<ChallengeParticipant> participants = challengeParticipantRepository.findByUserIdAndIsCompletedTrue(userId);

        return participants.stream()
                .filter(participant -> {
                    LocalDate completedDate = participant.getChallengeCompletedDate();
                    return completedDate.getYear() == year && completedDate.getMonthValue() == month;
                })
                .map(ChallengeParticipantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ChallengeRankingDto> getChallengeRankings(Long userId) {
        LocalDate today = LocalDate.now();

        // 로그인한 사용자의 오늘 날짜에 완료된 챌린지 수
        long myCompletedCount = challengeParticipantRepository.countByUserIdAndIsCompletedTrueAndChallengeCompletedDate(userId, today);
        // 현재 사용자 정보 가져오기
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 수락된 친구 요청 목록 가져오기
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


        // 친구들의 정보와 완료된 챌린지 수 가져오기
        List<ChallengeRankingDto> rankings = new ArrayList<>();

        // 친구들 정보 추가
        for (OtherUserDto otherUserDto : acceptedRequestsDtos) {
            Long friendId = otherUserDto.getId();
            long completedCount = challengeParticipantRepository.countByUserIdAndIsCompletedTrueAndChallengeCompletedDate(friendId, today);
            String name = otherUserDto.getName(); // 친구의 User 정보
            String profileUrl = otherUserDto.getProfileUrl();

            // 친구 정보와 완료된 챌린지 수를 DTO에 추가
            rankings.add(new ChallengeRankingDto(
                    friendId,
                    name,
                    profileUrl,
                    (int) completedCount,

                    0 // 초기 랭크는 0으로 설정
            ));
        }

        // 로그인한 사용자의 완료된 챌린지 수를 랭크에 포함
        rankings.add(new ChallengeRankingDto(
                userId,
                currentUser.getName(), // 현재 사용자 이름
                currentUser.getProfileUrl(), // 현재 사용자 프로필 URL
                (int) myCompletedCount,
                0 // 초기 랭크는 0으로 설정
        ));

        // 랭크 매기기
        rankings.sort((r1, r2) -> Integer.compare(r2.getCompletedCount(), r1.getCompletedCount()));

        int rank = 1; // 현재 랭크
        for (int i = 0; i < rankings.size(); i++) {
            // 첫 번째 사용자이거나 이전 사용자와 완료된 수가 다른 경우
            if (i == 0 || rankings.get(i).getCompletedCount() != rankings.get(i - 1).getCompletedCount()) {
                rankings.get(i).setRank(rank); // 현재 랭크 부여
                rank++; // 다음 랭크로 증가
            } else {
                // 이전 사용자와 완료된 수가 같으면 같은 랭크 부여
                rankings.get(i).setRank(rank - 1); // 이전 랭크 유지
            }
        }

        return rankings;
    }





}
