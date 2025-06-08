package com.example.Redi.logs.service;

import com.example.Redi.logs.data.Points;
import com.example.Redi.users.data.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PointsRepo extends MongoRepository<Points, String> {
    List<Points> findByReceiver(User receiver);
    List<Points> findByTimestampBetween(LocalDateTime from, LocalDateTime to);
    List<Points> findByReceiverAndTimestampBetween(User receiver, LocalDateTime from, LocalDateTime to);
}