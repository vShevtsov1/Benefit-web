package com.example.Redi.order.DTO;

import com.example.Redi.order.enums.OrderCreateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponseDTO {

    private String message;
    private OrderCreateType status;
    private OrderFullDTO orderFullDTO;
}
