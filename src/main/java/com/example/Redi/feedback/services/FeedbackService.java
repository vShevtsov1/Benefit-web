package com.example.Redi.feedback.services;

import com.example.Redi.email.EmailService;
import com.example.Redi.feedback.DTO.FeedbackUserDTO;
import com.example.Redi.feedback.data.Feedback;
import com.example.Redi.feedback.enums.FeedbackType;
import com.example.Redi.logs.data.Logs;
import com.example.Redi.logs.enums.LogType;
import com.example.Redi.logs.service.LogsService;
import com.example.Redi.users.data.User;
import com.example.Redi.users.services.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class FeedbackService {

    @Autowired
    private FeedbackRepo feedbackRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private LogsService logsService;
    public Feedback createNewFeedback(Feedback feedback,String email){
        User user =userRepo.findByEmail(email);
        if(user==null){
            return null;
        }

        feedback.setUser_id(user.getId());
        feedback.setFeedbackType(FeedbackType.NEW);
        feedbackRepo.save(feedback);
        CompletableFuture.runAsync(() ->emailService.sendEmailUserFeedback(user.getId(),feedback.getEmail(),feedback.getPhone_number(),feedback.getMessage()))
                .thenRun(() -> log.info("Email sent asynchronously!"))
                .exceptionally(ex -> {
                    log.error("Failed to send email", ex);
                    return null;
                });
        return feedback;
    }
    public Feedback updateFeedback(String feedbackId,FeedbackType feedbackType,String email){
        Feedback feedback = feedbackRepo.findById(feedbackId).get();
        if(feedback==null){
            return null;
        }
        String logMessage = String.format("Admin %s requested to change feedback status from %s to %s", email, feedback.getFeedbackType(), feedbackType);
        feedback.setFeedbackType(feedbackType);
        feedbackRepo.save(feedback);
        logsService.createLog(new Logs(LogType.FEEDBACK,email, LocalDateTime.now(),logMessage));
        return feedback;
    }
    public List<FeedbackUserDTO> getAll(){
        return feedbackRepo.getAllFeedback();
    }
}
