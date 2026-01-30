package com.solmed.solmedbackend.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PatchMapping("/updateUserPhone/{id}")
    public User patchUserPhone(@PathVariable Long id, @RequestBody Long phone){
        return userService.updateUserPhone(id,phone);
    }

    @GetMapping("/getAllUser")
    public Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    
    @GetMapping("/getUserById/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserId(id);
    }
}
