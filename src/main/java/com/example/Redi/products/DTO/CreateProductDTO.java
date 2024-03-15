package com.example.Redi.products.DTO;

import com.example.Redi.products.data.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDTO {

    private MultipartFile photoUrl;
    private String name;
    private String category;
    private String description;
    private double price;
}
