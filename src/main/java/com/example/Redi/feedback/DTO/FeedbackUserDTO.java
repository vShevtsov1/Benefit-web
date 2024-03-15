package com.example.Redi.feedback.DTO;

import com.example.Redi.feedback.enums.FeedbackType;
import com.example.Redi.users.DTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackUserDTO {
    private String _id;
    private String message;
    private UserDTO user_id;
    private String phone_number;
    private String email;
    private FeedbackType feedbackType;
}
