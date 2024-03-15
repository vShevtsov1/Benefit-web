package com.example.Redi.products.services;

import com.example.Redi.products.DTO.CreateProductDTO;
import com.example.Redi.products.DTO.UpdateProductDTO;
import com.example.Redi.products.data.Product;
import com.example.Redi.products.data.Review;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepo productsRepo;

    @Autowired
    private ModelMapper modelMapper;
    public Product createNewProduct(CreateProductDTO createProductDTO){
        Product product = modelMapper.map(createProductDTO, Product.class);
        productsRepo.save(product);
        return product;
    }
    public Product updateProduct(UpdateProductDTO updateProductDTO){
        Product product = productsRepo.findById(updateProductDTO.get_id()).get();
        if(product!=null){
            product.setName(updateProductDTO.getName());
            product.setCategory(updateProductDTO.getCategory());
            product.setDescription(updateProductDTO.getDescription());
            product.setPrice(updateProductDTO.getPrice());
            product.setAvailableQuantity(updateProductDTO.getAvailableQuantity());
            product.setVisible(updateProductDTO.getVisible());
            productsRepo.save(product);
            return product;
        }
        else{
            return null;
        }
    }

    public List<Product> getAllProducts(){
        return productsRepo.findAll();
    }
    public List<Product> getAllProductsByCategory(String category){
        return productsRepo.getProductsByCategory(category);
    }
    public Product applyReview(String product_id, Review review){
        Product product = productsRepo.findById(product_id).get();
        if(product==null){
            return null;
        }
        review.setDate(new Date());
        product.getReviews().add(review);
        productsRepo.save(product);
        return product;
    }



}
