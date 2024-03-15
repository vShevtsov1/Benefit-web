package com.example.Redi.feedback;

import com.example.Redi.feedback.DTO.FeedbackUserDTO;
import com.example.Redi.feedback.data.Feedback;
import com.example.Redi.feedback.enums.FeedbackType;
import com.example.Redi.feedback.services.FeedbackService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback, Authentication authentication){
        try {
            return new ResponseEntity<>(feedbackService.createNewFeedback(feedback,authentication.getPrincipal().toString()),HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/update")
    public ResponseEntity<Feedback> updateFeedBackStatus(@RequestParam("feedback_id") String feedback, @RequestParam("status")FeedbackType feedbackType,Authentication authentication){
        try {
            return new ResponseEntity<>(feedbackService.updateFeedback(feedback,feedbackType,authentication.getPrincipal().toString()),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<List<FeedbackUserDTO>> getALlFeedbacks(){
        try {
            return new ResponseEntity<>(feedbackService.getAll(),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
