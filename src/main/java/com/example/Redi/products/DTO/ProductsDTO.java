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
public class ProductsDTO {

    private String _id;
    private String photoUrl;
    private String name;
    private String category;
    private String country;
    private String priceDescription;
    private String description;
    private double price;
}
