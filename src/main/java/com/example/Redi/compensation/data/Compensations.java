package com.example.Redi.compensation.data;

import com.example.Redi.compensation.enums.CompensationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "compensations")
public class Compensations {

    private String _id;
    private List<String> images;
    private String message;
    private Integer count;
    private String user_id;
    private CompensationStatus compensationStatus;
}
