package com.example.beginnerfitbe.weight.service;

import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import com.example.beginnerfitbe.weight.domain.WeightRecord;
import com.example.beginnerfitbe.weight.dto.WeightRecordDto;
import com.example.beginnerfitbe.weight.repository.WeightRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeightRecordService {

    private final WeightRecordRepository weightRecordRepository;
    private final UserRepository userRepository;

    public WeightRecordDto create (Long userId, double weight){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        WeightRecord weightRecord = WeightRecord.builder()
                .user(user)
                .date(LocalDateTime.now())
                .weight(weight)
                .build();

        weightRecordRepository.save(weightRecord);

        return WeightRecordDto.fromEntity(weightRecord);
    }

    //시간순서대로
    public List<WeightRecordDto> list(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(weightRecordRepository.findByUserOrderByDateAsc(user).isEmpty()){
            return null;
        }
        return weightRecordRepository.findByUserOrderByDateAsc(user).stream()
                .map(WeightRecordDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<WeightRecordDto> getRecentWeightRecords(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(weightRecordRepository.findTop5ByUserOrderByDateDesc(user).isEmpty()){
            return null;
        }

        List<WeightRecord> recentWeightRecords = weightRecordRepository.findTop5ByUserOrderByDateDesc(user);

        recentWeightRecords.sort(Comparator.comparing(WeightRecord::getDate));

        return recentWeightRecords.stream()
                .map(WeightRecordDto::fromEntity)
                .collect(Collectors.toList());
    }
}
