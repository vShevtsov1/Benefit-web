package com.example.Redi.logs.data;


import com.example.Redi.users.data.User;
import com.example.Redi.users.enums.UpdatePointType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "points")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Points {

    private String id;

    @DBRef
    private User initiator;

    @DBRef
    private User receiver;

    private int points;

   private UpdatePointType updatePointType;

    private String message;

    private LocalDateTime timestamp;

    public Points(User initiator, User receiver, int points, UpdatePointType pointsType, String message, LocalDateTime timestamp) {
        this.initiator = initiator;
        this.receiver = receiver;
        this.points = points;
        this.updatePointType = pointsType;
        this.message = message;
        this.timestamp = timestamp;
    }


}
