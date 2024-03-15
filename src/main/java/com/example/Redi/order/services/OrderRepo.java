package com.example.Redi.order.services;

import com.example.Redi.order.DTO.OrderFullDTO;
import com.example.Redi.order.DTO.ProductQuantitySold;
import com.example.Redi.order.data.Order;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepo extends MongoRepository<Order,String> {

    @Aggregation(pipeline = {
            "{$match: { orderType: { $in: ?0 } }}",
            "{$addFields: { \"user_id\": { $toObjectId: '$user_id' } }}",
            "{$lookup: { from: \"Users\", localField: \"user_id\", foreignField: \"_id\", as: \"user_id\" }}",
            "{$set: { user: { $arrayElemAt: ['$user_id', 0]} }}",
            "{$unwind: { path: \"$products\" }}",
            "{$addFields: { \"products.product\": { $toObjectId: '$products.product' } }}",
            "{$lookup: { from: \"products\", localField: \"products.product\", foreignField: \"_id\", as: \"products.product\" }}",
            "{$set: { \"products.product\": { $arrayElemAt: ['$products.product', 0]} }}",
            "{$group: { _id: \"$_id\", time:{ $first: '$time' }, products: { $push: '$products' }, sum:{ $first: '$sum' }, orderType:{ $first: '$orderType' }, user:{ $first: '$user' } }}"
    })
    List<OrderFullDTO> getOrdersByStatus(List<String> status);

    @Aggregation(pipeline ={
            "{$match: { user_id: ?0 }}",
            "{$addFields: { \"user_id\": { $toObjectId: '$user_id' } }}",
            "{$lookup: { from: \"Users\", localField: \"user_id\", foreignField: \"_id\", as: \"user_id\" }}",
            "{$set: { user: { $arrayElemAt: ['$user_id', 0]} }}",
            "{$unwind: { path: \"$products\" }}",
            "{$addFields: { \"products.product\": { $toObjectId: '$products.product' } }}",
            "{$lookup: { from: \"products\", localField: \"products.product\", foreignField: \"_id\", as: \"products.product\" }}",
            "{$set: { \"products.product\": { $arrayElemAt: ['$products.product', 0]} }}",
            "{$group: { _id: \"$_id\", time:{ $first: '$time' }, products: { $push: '$products' }, sum:{ $first: '$sum' }, orderType:{ $first: '$orderType' }, user:{ $first: '$user' } }}"
    })
    List<OrderFullDTO> findByUser_id(String user_id);

    @Aggregation(pipeline = {
           " { $match: { time: { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: { path: '$products' } }",
            "{ $group: { _id: '$products.product', totalQuantitySold: { $sum: '$products.quantity' } } }",
            "{ $addFields: { _id: { $toObjectId: '$_id' } } }",
           " { $lookup: { from: 'products', localField: '_id', foreignField: '_id', as: 'product' } }",
            "{ $set: { product: { $arrayElemAt: ['$product', 0] } } }"

            }
    )
    List<ProductQuantitySold> calculateProductQuantitySoldBetweenDates(Date startDate, Date endDate);
    @Aggregation(pipeline = {
            "{ $match: { time: { $gte: ?0, $lte: ?1 }, 'products.product': ?2 } }",
            "{ $unwind: { path: '$products' } }",
            "{ $group: { _id: '$products.product', totalQuantitySold: { $sum: '$products.quantity' } } }",
            "{ $addFields: { _id: { $toObjectId: '$_id' } } }",
            "{ $lookup: { from: 'products', localField: '_id', foreignField: '_id', as: 'product' } }",
            "{ $set: { product: { $arrayElemAt: ['$product', 0] } } }"
    })
    List<ProductQuantitySold> calculateProductQuantitySoldBetweenDates(
            Date startDate,
            Date endDate,
            String productId
    );

    @Aggregation(pipeline ={
            "{$match: { user_id: ?0, time: { $gte: ?1, $lte: ?2 }}}",
            "{$addFields: { \"user_id\": { $toObjectId: '$user_id' } }}",
            "{$lookup: { from: \"Users\", localField: \"user_id\", foreignField: \"_id\", as: \"user_id\" }}",
            "{$set: { user: { $arrayElemAt: ['$user_id', 0]} }}",
            "{$unwind: { path: \"$products\" }}",
            "{$addFields: { \"products.product\": { $toObjectId: '$products.product' } }}",
            "{$lookup: { from: \"products\", localField: \"products.product\", foreignField: \"_id\", as: \"products.product\" }}",
            "{$set: { \"products.product\": { $arrayElemAt: ['$products.product', 0]} }}",
            "{$group: { _id: \"$_id\", time:{ $first: '$time' }, products: { $push: '$products' }, sum:{ $first: '$sum' }, orderType:{ $first: '$orderType' }, user:{ $first: '$user' } }}"
    })
    List<OrderFullDTO> findByUser_idAndTimeRange(String user_id, Date fromDate, Date toDate);


}
