package com.example.Redi.feedback.data;

import com.example.Redi.feedback.enums.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "feedback")
public class Feedback {

    private String _id;
    private String message;
    private String user_id;
    private String phone_number;
    private String email;
    private FeedbackType feedbackType;
}
