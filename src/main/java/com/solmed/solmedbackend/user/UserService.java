package com.solmed.solmedbackend.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solmed.solmedbackend.DTO.UserRequestDto;

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

    public UserRequestDto getUserName(Long id){
        User user = userrepo.findById(id).orElse(null);
        if (user!= null) {
            UserRequestDto userDto = new UserRequestDto(user.getId());
            return userDto;
        }
        return null;
    }

    public User getUserId(Long id){
        return userrepo.findById(id).orElse(null);
    }

    public Iterable<User> getAllUsers() {
        return userrepo.findAll();
    }

    public void deleteUserById(Long id) {
        userrepo.deleteById(id);
    }


}
