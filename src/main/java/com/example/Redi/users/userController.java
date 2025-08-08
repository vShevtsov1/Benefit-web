package com.example.Redi.users;

import com.example.Redi.users.DTO.*;
import com.example.Redi.users.data.User;
import com.example.Redi.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class userController {


    @Autowired
    private UserService userService;


    @PostMapping("/create")
    public ResponseEntity<UserCreateDTO> createNewUser(@RequestBody UserDTO userDTO)
    {
        try {
            return new ResponseEntity<>(userService.createNewUser(userDTO),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginNewUser(@RequestBody LoginDTO loginDTO){
        try {
            return new ResponseEntity<>(userService.loginUser(loginDTO),HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }
    @PostMapping("/password/update")
    public ResponseEntity<UpdatePasswordResponseDTO> loginNewUser(@RequestBody UpdatePasswordDTO updatePasswordDTO, Authentication authentication){
        try {
            return new ResponseEntity<>(userService.updateUserPassword(updatePasswordDTO,authentication.getPrincipal().toString()),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/update")
    public ResponseEntity<UserDTO> updateUserData(@ModelAttribute UpdateUserDTO userDTO, Authentication authentication){
        try {
            return new ResponseEntity<>(userService.updateUserData(userDTO,authentication.getPrincipal().toString()),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/update")
    public ResponseEntity<UserDTO> updateAdminUserData(@RequestBody AdminUpdateUserDataDTO userDTO,Authentication authentication){
        try {
            return new ResponseEntity<>(userService.updateUserDataAdmin(userDTO,authentication.getPrincipal().toString()),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/update/points")
    public ResponseEntity<UserDTO> updateUserPoints(@RequestBody UpdatePoints userDTO,Authentication authentication){
        try {
            return new ResponseEntity<>(userService.updateUserPoints(userDTO,authentication.getPrincipal().toString()),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/admin/delete")
    public void deleteUser(@RequestParam(value = "user_id") String user_id,Authentication authentication){
        userService.deleteUser(user_id,authentication.getPrincipal().toString());
    }

    @PostMapping("/uploadPhoto")
    public UserDTO uploadUserPhoto(@RequestPart("file") MultipartFile file, Authentication authentication) {
        try {
           return userService.uploadUserPhoto(file, authentication.getPrincipal().toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<UserDTO>> findAllUsers(){
        try {
            return new ResponseEntity<>(userService.findAllUsers(),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/request-reset-password")
    public ResponseEntity<RequestPasswordResponse> requestResetPassword(@RequestParam String email) {
        RequestPasswordResponse response = userService.requestResetPassword(email);
        if (response.isStatus()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/check-reset-token")
    public ResponseEntity<RequestPasswordResponse> checkResetToken(@RequestParam String token) {
        RequestPasswordResponse response = userService.checkResetToken(token);

        if (response.isStatus()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<RequestPasswordResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        RequestPasswordResponse response = userService.changePassword(request);

        if (response.isStatus()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
