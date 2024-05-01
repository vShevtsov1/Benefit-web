package com.example.Redi.products.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "products")
public class Product {

    private String _id;
    private String photoUrl;
    private String name;
    private String category;
    private String description;
    private double price;
    private String country;
    private String priceDescription;
    private int availableQuantity = Integer.MAX_VALUE;
    private int purchaseCount = 0;
    private List<Review> reviews = new ArrayList<>();
    private Boolean visible = true;

    public Product(String photoUrl, String name, String category, String description, double price,String country, int availableQuantity, int purchaseCount, List<Review> reviews, Boolean visible) {
        this.photoUrl = photoUrl;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.country = country;
        this.availableQuantity = availableQuantity;
        this.purchaseCount = purchaseCount;
        this.reviews = reviews;
        this.visible = visible;
    }
}
