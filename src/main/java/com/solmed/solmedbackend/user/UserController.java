package com.solmed.solmedbackend.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/getUserById/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserId(id);
    }
}
