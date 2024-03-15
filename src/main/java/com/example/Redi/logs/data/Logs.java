package com.example.Redi.logs.data;

import com.example.Redi.logs.enums.LogType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "logs")
public class Logs {

    private LogType logType;
    private String initiator;
    private LocalDateTime localDateTime;
    private String message;
}
