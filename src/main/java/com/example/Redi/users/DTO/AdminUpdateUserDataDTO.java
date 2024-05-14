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
public class AdminUpdateUserDataDTO {
    private String id;
    private String name;
    private String surname;
    private String department;
    private String division;
    private String country;
    private String position;
    private Date hireDate;
    private String email;
    private List<String> additionalEmails;
    private String phone;
    private String shippingAddress;
    private Role role;
}
