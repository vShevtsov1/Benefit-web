package com.example.Redi.email;

import com.example.Redi.order.DTO.ProductsOrderFullDTO;
import com.example.Redi.products.DTO.ProductsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import jakarta.mail.*;
import java.io.BufferedReader;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
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


    public void sendEmailOrder(String email,String user,LocalDateTime date, String address, List<ProductsOrderFullDTO> products,Double sum) {
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
            message.setSubject("Новый заказ от пользователя "+ user+" от "+date);

            String htmlContent = loadHTMLContentOrder( user,date,address,products,sum);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    private String loadHTMLContentOrder(String user, LocalDateTime date, String address, List<ProductsOrderFullDTO> products,Double sum) {
        String htmlContent = ""; // Read HTML content from your file or resource
        ClassPathResource resource = new ClassPathResource("confirm.html");
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

        htmlContent = htmlContent.replace("{{user}}", user);
        htmlContent = htmlContent.replace("{{date}}", date.toString());
        htmlContent = htmlContent.replace("{{address}}",address);
        htmlContent = htmlContent.replace("{{sum}}",sum.toString());

        StringBuilder productsHtml = new StringBuilder();
        for (ProductsOrderFullDTO productOrder : products) {
            ProductsDTO product = productOrder.getProduct();
            productsHtml.append("<tr>")
                    .append("<td>").append(product.getName()).append("</td>")
                    .append("<td>").append(product.getPrice()).append(" грн</td>")
                    .append("</tr>");
        }
        htmlContent = htmlContent.replace("{{products}}", productsHtml.toString());


        return htmlContent;
    }



    public void sendEmailUserPoints(String email,String type,Integer changeSumm,Integer curentSumm) {
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

            String htmlContent = loadHTMLContentUserPoints( type,changeSumm,curentSumm);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    private String loadHTMLContentUserPoints(String type, Integer changeSumm,Integer curentSumm) {
        String htmlContent = ""; // Read HTML content from your file or resource
        ClassPathResource resource = new ClassPathResource("balance.html");
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

        htmlContent = htmlContent.replace("{{type}}", type);
        htmlContent = htmlContent.replace("{{changeSumm}}", changeSumm.toString());
        htmlContent = htmlContent.replace("{{curentSumm}}",curentSumm.toString());
        return htmlContent;
    }


    public void sendEmailUserFeedback(String id,String email,String phone_number,String content) {
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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("a.kovalchuk@redi.partners"));
            message.setSubject("Новый отзыв от пользоватся "+email);

            String htmlContent = loadHTMLContentFeedback( id,email,phone_number,content);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    private String loadHTMLContentFeedback(String id,String email,String phone_number,String message) {
        String htmlContent = ""; // Read HTML content from your file or resource
        ClassPathResource resource = new ClassPathResource("feedback.html");
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

        htmlContent = htmlContent.replace("{{user_id}}", id);
        htmlContent = htmlContent.replace("{{email}}", email);
        htmlContent = htmlContent.replace("{{phone_number}}",phone_number);
        htmlContent = htmlContent.replace("{{message}}",message);
        htmlContent = htmlContent.replace("{{feedbackType}}","NEW");
        return htmlContent;
    }


    public void sendEmailResetPassword(String email,String link){
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
            message.setSubject("Сброс пароля для пользователя " + email);


            String htmlContent = loadHTMLContentReset( link);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String loadHTMLContentReset(String link) {
        String htmlContent = ""; // Read HTML content from your file or resource
        ClassPathResource resource = new ClassPathResource("resetpassword.html");
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

        htmlContent = htmlContent.replace("{{resetLink}}", link);
        return htmlContent;
    }



}
