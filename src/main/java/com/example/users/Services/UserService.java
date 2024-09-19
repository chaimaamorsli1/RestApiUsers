package com.example.users.Services;

import com.example.users.Entities.User;
import com.example.users.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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
        userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("entity not found"));
        userRepository.deleteById(id);
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new EntityNotFoundException("entity not found"));
        if(user.getPassword().equals(password)){
            return user;
        }
        throw new RuntimeException("wrong password");
    }
}
