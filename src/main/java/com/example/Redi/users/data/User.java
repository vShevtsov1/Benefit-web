package com.example.Redi.users.data;

import com.example.Redi.users.enums.EmploymentType;
import com.example.Redi.users.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "Users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private String id;
    private String name;
    private String surname;
    private String password;
    private String department;
    private String division;
    private String position;
    private Date hireDate;
    private int bonusCount;
    private String country;
    @Indexed(unique = true)
    private String email;
    private List<String> additionalEmails;
    private String phone;
    private String shippingAddress;
    private String photoUrl;
    private Role role;
    private Boolean changePassword;
    private EmploymentType employmentType;

}
