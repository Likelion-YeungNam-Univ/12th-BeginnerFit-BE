package com.example.beginnerfitbe.alarm.repository;

import com.example.beginnerfitbe.alarm.domain.Alarm;
import com.example.beginnerfitbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByUser(User user);
}
