package com.example.beginnerfitbe.weight.service;

import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import com.example.beginnerfitbe.weight.domain.WeightRecord;
import com.example.beginnerfitbe.weight.dto.WeightRecordDto;
import com.example.beginnerfitbe.weight.repository.WeightRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

}
