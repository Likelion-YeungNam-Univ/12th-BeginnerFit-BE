package com.example.beginnerfitbe.challengeparticipant.service;

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

    @Transactional(readOnly = true)
    public List<ChallengeParticipantDTO> getUserChallenges(Long userId) {


        // 현재 날짜
        LocalDate currentDate = LocalDate.now();

        // userId와 현재 날짜로 ChallengeParticipant 리스트 반환
        List<ChallengeParticipant> participants = challengeParticipantRepository.findByUserIdAndChallengeCompletedDate(userId, currentDate);

        // DTO로 변환하여 반환
        return participants.stream()
                .map(ChallengeParticipantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void completeChallenge(Long userId, Long challengeId) {
        LocalDate currentDate = LocalDate.now();

        // 오늘 날짜이면서 주어진 challengeId와 userId에 해당하는 참가자 찾기
        ChallengeParticipant participant = challengeParticipantRepository
                .findByUserIdAndChallenge_ChallengeIdAndChallengeCompletedDate(userId, challengeId, currentDate)
                .orElseThrow(() -> new RuntimeException("Challenge participant not found")); // 예외 처리

        // isCompleted를 true로 변경
        participant.setCompleted(true); // setter 메서드 필요
        challengeParticipantRepository.save(participant); // 변경사항 저장
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

        // 랭크 매기기
        rankings.sort((r1, r2) -> Integer.compare(r2.getCompletedCount(), r1.getCompletedCount()));
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1); // 순위는 1부터 시작
        }

        // 로그인한 사용자의 완료된 챌린지 수를 랭크에 포함
        rankings.add(new ChallengeRankingDto(
                userId,
                currentUser.getName(), // 현재 사용자 이름
                currentUser.getProfileUrl(), // 현재 사용자 프로필 URL
                (int) myCompletedCount,
                0 // 초기 랭크는 0으로 설정
        ));

        // 전체 리스트 다시 정렬
        rankings.sort((r1, r2) -> Integer.compare(r2.getCompletedCount(), r1.getCompletedCount()));
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }

        return rankings;
    }
}
