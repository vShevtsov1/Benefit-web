package com.example.Redi.compensation.services;

import com.example.Redi.compensation.DTO.CompensationsUserDTO;
import com.example.Redi.compensation.data.Compensations;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CompensationsRepo extends MongoRepository<Compensations,String> {

    @Aggregation(pipeline = {
            "{$match: { user_id: ?0 }}",
            "{ $addFields: { user_id: { $toObjectId: '$user_id' } } }",
            " {$lookup:{from:'Users', localField:'user_id', foreignField:'_id', as:'user'}}",
            "{$set:{user_id:{$arrayElemAt:['$user', 0]}}}",
            "{ $addFields: { orderId: { $toObjectId: '$orderId' } } }",
            " {$lookup:{from:'Order', localField:'orderId', foreignField:'_id', as:'Order'}}",
            "{$set:{orderId:{$arrayElemAt:['$Order', 0]}}}"
    })
    List<CompensationsUserDTO> findByUser_id(String userId);


    @Aggregation(pipeline = {
            "{ $addFields: { user_id: { $toObjectId: '$user_id' } } }",
            " {$lookup:{from:'Users', localField:'user_id', foreignField:'_id', as:'user'}}",
            "{$set:{user_id:{$arrayElemAt:['$user', 0]}}}",
            "{ $addFields: { orderId: { $toObjectId: '$orderId' } } }",
            " {$lookup:{from:'Order', localField:'orderId', foreignField:'_id', as:'Order'}}",
            "{$set:{orderId:{$arrayElemAt:['$Order', 0]}}}"
    })
    List<CompensationsUserDTO> getAllCompensations();
}
