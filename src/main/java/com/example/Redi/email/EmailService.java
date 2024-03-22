package com.example.Redi.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    @Value("${email.gmail.user}")
    private String gmailUser;

    @Value("${email.gmail.password}")
    private String gmailPassword;

    public void sendEmail(String email,String password) {

        String from = gmailUser;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(gmailUser, gmailPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            message.setSubject("Registration password " + email + " in Redi benefit app");

            String content = "Your registration password " + password;
            message.setText(content);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully...");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
