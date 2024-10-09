package com.example.users.Controllers;

import com.example.users.DTO.LoginRequest;
import com.example.users.Entities.User;
import com.example.users.Services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200/",allowedHeaders = "*")
@RestController
@RequestMapping(path = "user")
public class userController {

    private UserService userService;

    public userController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }
    @PostMapping
    public void addUser(@RequestBody User user){
        userService.addUser(user);
    }
    @PutMapping
    public void updateUser( @RequestParam int id,@RequestBody User user){
        userService.updateUser(id,user);
    }
    @DeleteMapping
    public void deleteuser(@RequestParam int id ){
        userService.deleteUser(id);
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }
    @GetMapping("/OneUser")
    public User getOneeUser(@RequestParam Integer id){
        return  userService.getOneUser(id);
    }



}
