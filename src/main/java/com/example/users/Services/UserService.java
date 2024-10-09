package com.example.users.Services;

import com.example.users.Entities.User;
import com.example.users.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(int id, User user) {
        User u = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("entity not found"));
        u.setUsername(user.getUsername());
        u.setPassword(user.getPassword());
        u.setRole(user.getRole());
        userRepository.save(u);
    }

    public void deleteUser(int id) {
        userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("entity not found"));
        userRepository.deleteById(id);
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("entity not found"));
        if (user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("wrong password");
    }

    public User getOneUser(Integer id) {
        return userRepository.findById(id).
                orElseThrow(() -> new RuntimeException("User not found with id: " + id));

    }

    private static final String SECRET_KEY = "secretKEY"; // Keep this safe
    private static final long EXPIRATION_TIME = 3600000; // 1 hour in milliseconds


    public String generateResetToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String email, String token) {
        String resetLink = "http://localhost:4200/reset-password?token= " + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("smtp@mailtrap.io");
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetLink);
        mailSender.send(message);
    }

    public void resetPassword(String token, String newPassword) {
        String email = validateToken(token); // Validate and get email from token
        if (email == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();
        user.setPassword(hashPassword(newPassword)); // Ensure to hash the new password
        userRepository.save(user);
    }

    private String validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // Return the email from claims
        } catch (Exception e) {
            return null; // Token is invalid or expired
        }
    }

    private String hashPassword(String password) {
        // Implement password hashing (e.g., using BCrypt)
        //return BCrypt.hashpw(password, BCrypt.gensalt());
        return "";
    }
}

