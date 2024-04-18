package com.example.Redi.products;

import com.example.Redi.products.DTO.CreateProductDTO;
import com.example.Redi.products.DTO.UpdateProductDTO;
import com.example.Redi.products.data.Product;
import com.example.Redi.products.data.Review;
import com.example.Redi.products.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@ModelAttribute CreateProductDTO createProductDTO){
        try {
            return new ResponseEntity<>(productsService.createNewProduct(createProductDTO),HttpStatus.CREATED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Product> updateProduct(@RequestBody UpdateProductDTO updateProductDTO){
        try {
            return new ResponseEntity<>(productsService.updateProduct(updateProductDTO),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/get-all")
    public ResponseEntity<List<Product>> getAllProducts(){
        try {
            return new ResponseEntity<>(productsService.getAllProducts(),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/get-all/category")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam("category")String category){
        try {
            return new ResponseEntity<>(productsService.getAllProductsByCategory(category),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/apply-review")
    public ResponseEntity<Product> applyReviews(@RequestParam("product_id")String product_id, @RequestBody Review review){
        try {
            return new ResponseEntity<>(productsService.applyReview(product_id,review),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity deleteProduct(@RequestParam("product_id")String product_id){
        try {
            productsService.deleteProduct(product_id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
