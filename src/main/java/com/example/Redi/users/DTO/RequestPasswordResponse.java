package com.example.Redi.users.DTO;

import com.example.Redi.users.enums.ResetPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPasswordResponse {

    private String message;
    private boolean status;
    private ResetPassword resetStatus;
}
