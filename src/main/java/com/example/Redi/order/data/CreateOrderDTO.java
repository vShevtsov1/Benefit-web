package com.example.Redi.order.data;

import com.example.Redi.order.DTO.ProductsOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {

    private List<ProductsOrderDTO> products;
}
