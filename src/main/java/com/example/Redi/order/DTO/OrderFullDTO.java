package com.example.Redi.order.DTO;

import com.example.Redi.order.enums.OrderType;
import com.example.Redi.users.DTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFullDTO {

    private String _id;
    private LocalDateTime time;
    private List<ProductsOrderFullDTO> products;
    private Double sum;
    private OrderType orderType;
    private UserDTO user;
}
