package com.example.users.Controllers;

import com.example.users.Entities.PasswordResetRequest;
import com.example.users.Entities.ResetPasswordRequest;
import com.example.users.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    @Autowired
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/request-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody PasswordResetRequest request){
        String token = userService.generateResetToken(request.getEmail());
        userService.sendResetPasswordEmail(request.getEmail(), token);
        return ResponseEntity.ok("Password email reset sent/token: " + token);
    }

    @PostMapping("/reset-Password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request){
        try {
            userService.resetPassword(request.getToken(),request.getNewPassword());
            return ResponseEntity.ok("Password Updated successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("invalid token or user ");
        }
    }
}
