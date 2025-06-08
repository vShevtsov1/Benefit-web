package com.example.Redi.logs.dto;

import com.example.Redi.logs.data.Points;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointsDTO {
    private List<Points> content;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;

}
