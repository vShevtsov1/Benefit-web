package com.example.Redi.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import javax.mail.*;
import java.io.BufferedReader;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailService {

    @Value("${email.gmail.user}")
    private String gmailUser;

    @Value("${email.gmail.password}")
    private String gmailPassword;

    public void sendEmail(String email,String password) {



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
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(gmailUser, "Benefit REDI"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Реєстрація на сайті Benefit REDI");

            String htmlContent = loadHTMLContent( password);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    private String loadHTMLContent( String password) {
        String htmlContent = ""; // Read HTML content from your file or resource
        ClassPathResource resource = new ClassPathResource("index.html");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            htmlContent = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace placeholders with actual values
        htmlContent = htmlContent.replace("{{userPassword}}", password);

        return htmlContent;
    }
}
