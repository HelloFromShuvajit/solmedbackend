package com.solmed.solmedbackend.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public User postUser(@RequestBody User user) {
         return userService.addUser(user);
        
    }
    
    @GetMapping("/getUserById/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserId(id);
    }
}
