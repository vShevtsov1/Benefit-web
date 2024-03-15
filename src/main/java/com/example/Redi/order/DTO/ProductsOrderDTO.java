package com.example.Redi.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsOrderDTO {
    private String product;
    private Integer quantity;
}
