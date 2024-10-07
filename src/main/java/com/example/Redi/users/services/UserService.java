package com.example.Redi.users.services;

import com.example.Redi.email.EmailService;
import com.example.Redi.logs.data.Logs;
import com.example.Redi.logs.enums.LogType;
import com.example.Redi.logs.service.LogsService;
import com.example.Redi.s3.S3Service;
import com.example.Redi.security.TokenServices;
import com.example.Redi.users.DTO.*;
import com.example.Redi.users.data.User;
import com.example.Redi.users.enums.CreateUser;
import com.example.Redi.users.enums.LoginUser;
import com.example.Redi.users.enums.UpdatePointType;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.passay.DictionarySubstringRule.ERROR_CODE;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogsService logsService;

    @Autowired
    private TokenServices tokenServices;

    @Autowired
    private S3Service service;

    @Autowired
    private EmailService emailService;
    public String generatePassayPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(10, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        return password;
    }
    public UserCreateDTO createNewUser(UserDTO userDTO){
        if(!userRepo.existsByEmail(userDTO.getEmail())) {
            User user = modelMapper.map(userDTO, User.class);
            String password = generatePassayPassword();
            emailService.sendEmail(userDTO.getEmail(),password);
            user.setPassword(passwordEncoder.encode(password));
            user.setChangePassword(true);
            userRepo.save(user);
            logsService.createLog(new Logs(LogType.REGISTER, null, LocalDateTime.now(), "User successfully registered with email: " + userDTO.getEmail() + " and ID: " + user.getId()));
            return new UserCreateDTO("User successfully registered", CreateUser.OK,modelMapper.map(user, UserDTO.class));
        }
        logsService.createLog(new Logs(LogType.REGISTER, null, LocalDateTime.now(), "Attempted registration failed. User with email: " + userDTO.getEmail() + " already exists"));

        return new UserCreateDTO("The user with this email is already registered", CreateUser.USER_EXIST,null);
    }

    public LoginResponseDTO loginUser(LoginDTO loginDTO){
        User user = userRepo.findByEmail(loginDTO.getEmail());
        if(user == null){
            return new LoginResponseDTO("User with the provided email does not exist", LoginUser.USER_NOT_EXIST,null,null);
        }
        if(user.getChangePassword() && passwordEncoder.matches(loginDTO.getPassword(),user.getPassword())){
            return new LoginResponseDTO("Password change is required",LoginUser.PASSWORD_CHANGE_REQUIRED,tokenServices.generateTokenUser(user, Date.from(Instant.now().plus(10, ChronoUnit.MINUTES))),null);
        }
        if(passwordEncoder.matches(loginDTO.getPassword(),user.getPassword())){
            return new LoginResponseDTO("Login successful",LoginUser.OK,tokenServices.generateTokenUser(user, Date.from(Instant.now().plus(1, ChronoUnit.DAYS))),modelMapper.map(user,UserDTO.class));

        }
        else {
            return new LoginResponseDTO("Wrong login data",LoginUser.FAILED,null,null);
        }
    }
    public UpdatePasswordResponseDTO updateUserPassword(UpdatePasswordDTO updatePasswordDTO, String email){
        User user = userRepo.findByEmail(email);
        if(user == null){
            return new UpdatePasswordResponseDTO("User with the provided email does not exist", LoginUser.USER_NOT_EXIST);
        }
        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getPassword()));
        user.setChangePassword(false);
        userRepo.save(user);
        return new UpdatePasswordResponseDTO("Password updated successfully", LoginUser.OK);
    }

    public UserDTO updateUserData(UpdateUserDTO userDTO,String email){
        User user = userRepo.findByEmail(email);
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setAdditionalEmails(userDTO.getAdditionalEmails());
        user.setPhone(userDTO.getPhone());
        user.setCountry(userDTO.getCountry());
        user.setShippingAddress(userDTO.getShippingAddress());
        userRepo.save(user);
        return modelMapper.map(user,UserDTO.class);
    }
    public UserDTO updateUserDataAdmin(AdminUpdateUserDataDTO userDTO,String email){
        User user = userRepo.findById(userDTO.getId()).get();
        if(user!=null){
            user.setName(userDTO.getName());
            user.setSurname(userDTO.getSurname());
            user.setDepartment(userDTO.getDepartment());
            user.setDivision(userDTO.getDivision());
            user.setCountry(userDTO.getCountry());
            user.setPosition(userDTO.getPosition());
            user.setHireDate(userDTO.getHireDate());
            user.setEmail(userDTO.getEmail());
            user.setAdditionalEmails(userDTO.getAdditionalEmails());
            user.setPhone(userDTO.getPhone());
            user.setShippingAddress(userDTO.getShippingAddress());
            user.setRole(userDTO.getRole());
            userRepo.save(user);
            String logMessage = "Admin updated data for user with ID: " + user.getId() + "\n";
            logMessage += "New Data:\n";
            logMessage += "Name: " + user.getName() + "\n";
            logMessage += "Surname: " + user.getSurname() + "\n";
            logMessage += "Department: " + user.getDepartment() + "\n";
            logMessage += "Division: " + user.getDivision() + "\n";
            logMessage += "Country: " + user.getCountry() + "\n";
            logMessage += "Position: " + user.getPosition() + "\n";
            logMessage += "Hire Date: " + user.getHireDate() + "\n";
            logMessage += "Email: " + user.getEmail() + "\n";
            logMessage += "Additional Emails: " + user.getAdditionalEmails() + "\n";
            logMessage += "Phone: " + user.getPhone() + "\n";
            logMessage += "Shipping Address: " + user.getShippingAddress() + "\n";
            logMessage += "Role: " + user.getRole() + "\n";
            logsService.createLog(new Logs(LogType.USER,email,LocalDateTime.now(),logMessage));
            return modelMapper.map(user,UserDTO.class);
        }
        else {
            return null;
        }

    }

    public UserDTO updateUserPoints(UpdatePoints updatePoints,String email){
        User user = userRepo.findById(updatePoints.getUserId()).get();
        if(user!=null){
            if(updatePoints.getType().equals(UpdatePointType.INCREASE)){
                user.setBonusCount(user.getBonusCount()+updatePoints.getCount());
                userRepo.save(user);

                CompletableFuture.runAsync(() ->emailService.sendEmailUserPoints(user.getEmail(),"збільшено",updatePoints.getCount(),user.getBonusCount()))
                        .thenRun(() -> log.info("Email sent asynchronously!"))
                        .exceptionally(ex -> {
                            log.error("Failed to send email", ex);
                            return null;
                        });
            }
            else {
                user.setBonusCount(user.getBonusCount()-updatePoints.getCount());
                userRepo.save(user);
                CompletableFuture.runAsync(() ->emailService.sendEmailUserPoints(user.getEmail(),"зменшено",updatePoints.getCount(),user.getBonusCount()))
                        .thenRun(() -> log.info("Email sent asynchronously!"))
                        .exceptionally(ex -> {
                            log.error("Failed to send email", ex);
                            return null;
                        });

            }

            String logMessage = "Admin updated points for user with ID: " + user.getId() + "\n";
            logMessage += "Message: " + updatePoints.getMessage() + "\n";
            logMessage += "Type: " + updatePoints.getType() + "\n";
            logMessage += "Count: " + updatePoints.getCount() + "\n";

            logsService.createLog(new Logs(LogType.POINTS,email,LocalDateTime.now(),logMessage));
            return modelMapper.map(user,UserDTO.class);
        }
        return null;
    }
    public void deleteUser(String userId, String email) {
        userRepo.deleteById(userId);

        String logMessage = "Admin with email "+email+" deleted user with ID: " + userId;

        logsService.createLog(new Logs(LogType.USER, email, LocalDateTime.now(), logMessage));
    }


    public UserDTO uploadUserPhoto(MultipartFile multipartFile,String email) throws IOException {
        User user = userRepo.findByEmail(email);
        user.setPhotoUrl(service.uploadPhoto("users",multipartFile));
        userRepo.save(user);
        return modelMapper.map(user,UserDTO.class);

    }

    public List<UserDTO> findAllUsers() {
        List<User> userList = userRepo.findAll();
        return userList.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }




}
