package com.example.Redi.feedback.services;

import com.example.Redi.compensation.DTO.CompensationsUserDTO;
import com.example.Redi.feedback.DTO.FeedbackUserDTO;
import com.example.Redi.feedback.data.Feedback;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FeedbackRepo extends MongoRepository<Feedback,String> {

    @Aggregation(pipeline = {
            "{ $addFields: { user_id: { $toObjectId: '$user_id' } } }",
            " {$lookup:{from:'Users', localField:'user_id', foreignField:'_id', as:'user'}}",
            "{$set:{user_id:{$arrayElemAt:['$user', 0]}}}"
    })
    List<FeedbackUserDTO> getAllFeedback();
}
