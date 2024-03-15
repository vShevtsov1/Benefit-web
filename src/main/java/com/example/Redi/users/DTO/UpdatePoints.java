package com.example.Redi.users.DTO;

import com.example.Redi.users.enums.UpdatePointType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePoints {

    private String message;
    private UpdatePointType type;
    private String userId;
    private Integer count;
}
