package com.example.Redi.products.services;

import com.example.Redi.products.data.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductsRepo extends MongoRepository<Product,String> {
    List<Product> getProductsByCategory(String category);
}
