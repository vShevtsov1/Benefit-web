package com.example.Redi.logs;

import com.example.Redi.logs.data.Points;
import com.example.Redi.logs.dto.PointsDTO;
import com.example.Redi.logs.service.PointsService;
import com.example.Redi.users.data.User;
import com.example.Redi.users.services.UserRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/points")
@Tag(name = "Matrix API", description = "API для получения проектов и юнитов")
public class PointsController {

    @Autowired
    private PointsService pointsService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    @Operation(summary = "Получить здания проекта", description = "Возвращает список зданий для указанного проекта и ресурса")
    public ResponseEntity<PointsDTO> getPoints(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,

            @RequestParam(required = false) String userId,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PointsDTO response = pointsService.getAll(from, to, userId, page, size);
        return ResponseEntity.ok(response);
    }






}
