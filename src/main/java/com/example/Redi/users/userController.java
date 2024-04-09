package com.example.Redi.users;

import com.example.Redi.users.DTO.*;
import com.example.Redi.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    }

    @PostMapping("/uploadPhoto")
    public void uploadUserPhoto(@RequestPart("file") MultipartFile file, Authentication authentication) {
        try {
            userService.uploadUserPhoto(file, authentication.getPrincipal().toString());
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
