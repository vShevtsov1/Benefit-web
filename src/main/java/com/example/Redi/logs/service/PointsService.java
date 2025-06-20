package com.example.Redi.logs.service;

import com.example.Redi.logs.data.Points;
import com.example.Redi.logs.dto.PointsDTO;
import com.example.Redi.s3.DTO.UploadResult;
import com.example.Redi.s3.ReportS3Service;
import com.example.Redi.s3.S3Service;
import com.example.Redi.users.data.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PointsService {

    @Autowired
    private PointsRepo pointsRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ReportS3Service reportS3Service;

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


    public UploadResult exportPointsToExcel(LocalDateTime from, LocalDateTime to, String userId) throws IOException {
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
                Aggregation.skip(0L),
                Aggregation.limit((long) Integer.MAX_VALUE)
        );

        List<Points> records = mongoTemplate.aggregate(aggregation, "points", Points.class).getMappedResults();

        Workbook workbook = generateReport(records);
        try {
            return reportS3Service.uploadExcel("exports", workbook);
        } finally {
            workbook.close();
        }
    }

    public Workbook generateReport(List<Points> records) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook ();
        Sheet sheet = workbook.createSheet("Бали");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Ініціатор");
        headerRow.createCell(1).setCellValue("Отримувач");
        headerRow.createCell(2).setCellValue("Департамент");
        headerRow.createCell(3).setCellValue("Бали");
        headerRow.createCell(4).setCellValue("Тип");
        headerRow.createCell(5).setCellValue("Повідомлення");
        headerRow.createCell(6).setCellValue("Дата");

        int rowNum = 1;
        for (Points record : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.getInitiator() != null ? record.getInitiator().getName() + " " + record.getInitiator().getSurname() + " (" + record.getInitiator().getEmail() + ")" : "Система");
            row.createCell(1).setCellValue(record.getReceiver() != null ? record.getReceiver().getName() + " " + record.getReceiver().getSurname() + " (" + record.getReceiver().getEmail() + ")" : "Не відомий користувач");
            row.createCell(2).setCellValue(record.getReceiver().getDepartment());
            row.createCell(3).setCellValue(record.getPoints());
            row.createCell(4).setCellValue(record.getUpdatePointType().name());
            row.createCell(5).setCellValue(record.getMessage());
            row.createCell(6).setCellValue(record.getTimestamp().toString());
        }

        for (int i = 0; i <= 6; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }



}
