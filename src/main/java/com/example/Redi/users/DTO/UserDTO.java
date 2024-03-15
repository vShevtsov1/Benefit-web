package com.example.Redi.users.DTO;

import com.example.Redi.users.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String name;
    private String surname;
    private String department;
    private String division;
    private String position;
    private Date hireDate;
    private int bonusCount;
    private String email;
    private List<String> additionalEmails;
    private String phone;
    private String shippingAddress;
    private String photoUrl;
    private Role role;
}
