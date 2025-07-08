package com.example.Redi.logs.service;

import com.example.Redi.logs.data.Points;
import com.example.Redi.users.data.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PointsRepo extends MongoRepository<Points, String> {
    List<Points> findByReceiver(User receiver);
    List<Points> findByTimestampBetween(LocalDateTime from, LocalDateTime to);
    List<Points> findByReceiverAndTimestampBetween(User receiver, LocalDateTime from, LocalDateTime to);



    List<Points> findByTimestampGreaterThanEqual(LocalDateTime from);

    List<Points> findByTimestampLessThanEqual(LocalDateTime to);
    List<Points> findByReceiver_Id(ObjectId receiverId);


    List<Points> findByReceiver_Id(String userId);

}