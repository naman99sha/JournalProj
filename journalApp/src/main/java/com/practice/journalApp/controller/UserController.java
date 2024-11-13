package com.practice.journalApp.controller;

import com.practice.journalApp.entity.User;
import com.practice.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        userService.saveEntry(user);
        return user;
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
        User userInDb = userService.findByUserName(user.getUserName());
        if(userInDb!=null){
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            userService.saveEntry(userInDb);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
