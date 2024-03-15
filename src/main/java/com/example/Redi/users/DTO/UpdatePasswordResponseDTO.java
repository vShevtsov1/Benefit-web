package com.example.Redi.users.DTO;

import com.example.Redi.users.enums.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordResponseDTO {
    private String message;
    private LoginUser status;
}
