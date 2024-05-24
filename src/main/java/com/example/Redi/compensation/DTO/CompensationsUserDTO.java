package com.example.Redi.compensation.DTO;

import com.example.Redi.compensation.enums.CompensationStatus;
import com.example.Redi.order.data.Order;
import com.example.Redi.users.DTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompensationsUserDTO {

    private String _id;
    private List<String> images;
    private String message;
    private Integer count;
    private UserDTO user_id;
    private Order orderId;
    private CompensationStatus compensationStatus;
}
