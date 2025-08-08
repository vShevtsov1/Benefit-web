package com.example.Redi.users.DTO;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String token;
    private String newPassword;
}
