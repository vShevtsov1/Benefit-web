package com.example.Redi.users.DTO;

import com.example.Redi.users.enums.CreateUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    private String message;

    private CreateUser status;
    private UserDTO userDTO;
}
