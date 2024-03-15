package com.example.Redi.compensation.DTO;

import com.example.Redi.compensation.enums.CompensationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompensationsDTO {

    private List<String> images;
    private String message;
    private Integer count;
}
