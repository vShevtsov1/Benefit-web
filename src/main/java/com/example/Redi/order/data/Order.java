package com.example.Redi.order.data;

import com.example.Redi.order.DTO.ProductsOrderDTO;
import com.example.Redi.order.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "Order")
public class Order {
    private String _id;
    private LocalDateTime time;
    private List<ProductsOrderDTO> products;
    private Double sum;
    private OrderType orderType;
    private String user_id;

    public Order(LocalDateTime time, List<ProductsOrderDTO> products, Double sum, OrderType orderType, String user_id) {
        this.time = time;
        this.products = products;
        this.sum = sum;
        this.orderType = orderType;
        this.user_id = user_id;
    }
}
