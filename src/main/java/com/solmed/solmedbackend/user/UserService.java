package com.solmed.solmedbackend.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired 
    private UserRepository userrepo;

    public User addUser(User user){
        return userrepo.save(user);
    }

    public User updateUserPhone(Long id, Long phone){
        User updateUser = userrepo.findById(id).orElse(null);
        if (updateUser!= null) {
            updateUser.setPhone(phone);
            return userrepo.save(updateUser);
        }
        return null;
    }


    public User getUserId(Long id){
        return userrepo.findById(id).orElse(null);
    }

    public Iterable<User> getAllUsers() {
        return userrepo.findAll();
    }


}
