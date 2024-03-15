package com.example.Redi.order.DTO;

import com.example.Redi.products.DTO.ProductsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsOrderFullDTO {

    private ProductsDTO product;
    private Integer quantity;
}
