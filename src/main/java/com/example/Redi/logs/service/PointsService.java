package com.example.Redi.logs.service;

import com.example.Redi.logs.data.Points;
import com.example.Redi.logs.dto.PointsDTO;
import com.example.Redi.users.data.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PointsService {

    @Autowired
    private PointsRepo pointsRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    public PointsDTO getAll(LocalDateTime from, LocalDateTime to, String userId, int page, int size) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (from != null && to != null) {
            criteriaList.add(Criteria.where("timestamp").gte(from).lte(to));
        } else if (from != null) {
            criteriaList.add(Criteria.where("timestamp").gte(from));
        } else if (to != null) {
            criteriaList.add(Criteria.where("timestamp").lte(to));
        }

        if (userId != null) {
            criteriaList.add(Criteria.where("receiver.$id").is(userId));
        }

        Criteria criteria = criteriaList.isEmpty()
                ? new Criteria()
                : new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));

        MatchOperation match = Aggregation.match(criteria);

        Aggregation countAgg = Aggregation.newAggregation(match);
        long totalElements = mongoTemplate.aggregate(countAgg, "points", Points.class).getMappedResults().size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        Aggregation aggregation = Aggregation.newAggregation(
                match,
                Aggregation.addFields()
                        .addFieldWithValue("initiatorOid", ConvertOperators.ToObjectId.toObjectId("$initiator.$id"))
                        .build(),
                Aggregation.addFields()
                        .addFieldWithValue("receiverOid", ConvertOperators.ToObjectId.toObjectId("$receiver.$id"))
                        .build(),
                Aggregation.lookup("Users", "initiatorOid", "_id", "initiator"),
                Aggregation.unwind("initiator", true),
                Aggregation.lookup("Users", "receiverOid", "_id", "receiver"),
                Aggregation.unwind("receiver", true),
                Aggregation.project("timestamp", "points", "message", "updatePointType")
                        .and("initiator._id").as("initiator._id")
                        .and("initiator.name").as("initiator.name")
                        .and("initiator.surname").as("initiator.surname")
                        .and("initiator.email").as("initiator.email")
                        .and("initiator.department").as("initiator.department")
                        .and("initiator.division").as("initiator.division")
                        .and("initiator.position").as("initiator.position")

                        .and("receiver._id").as("receiver._id")
                        .and("receiver.name").as("receiver.name")
                        .and("receiver.surname").as("receiver.surname")
                        .and("receiver.email").as("receiver.email")
                        .and("receiver.department").as("receiver.department")
                        .and("receiver.division").as("receiver.division")
                        .and("receiver.position").as("receiver.position"),


                Aggregation.sort(Sort.Direction.DESC, "timestamp"),
                Aggregation.skip((long) page * size),
                Aggregation.limit(size)
        );

        List<Points> content = mongoTemplate.aggregate(aggregation, "points", Points.class).getMappedResults();

        return new PointsDTO(content, totalElements, totalPages, page, size);
    }


    public void createPoints(Points points) {
        if (points.getTimestamp() == null) {
            points.setTimestamp(LocalDateTime.now());
        }
        pointsRepo.save(points);
    }


}
