package com.example.Redi.users.DTO;

import com.example.Redi.users.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private String name;
    private String surname;
    private List<String> additionalEmails;
    private String phone;
    private String country;
    private String shippingAddress;

}
