package com.example.Redi.products.DTO;

import com.example.Redi.products.data.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductDTO {
    private String _id;
    private String name;
    private String category;
    private String description;
    private double price;
    private int availableQuantity;
    private Boolean visible;
}
