package com.solmed.solmedbackend.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired 
    private UserRepository userrepo;

    public User getUserId(Long id){
        return userrepo.findById(id).orElse(null);
    }

}
