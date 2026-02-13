package com.solmed.solmedbackend.user;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    
    @Autowired 
    private UserRepository userrepo;

    public User addUser(User user){
        return userrepo.save(user);
    }

    public String getUserName(Long id){
        User user = userrepo.findById(id).orElse(null);
        if (user!= null) {
            String userName=user.getName();
            return userName;
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

    public User updateUser(Long id, User user) {
        User current = userrepo.findById(id).orElse(null);
        if (current!= null) {
            current.setAge(user.getAge());
            current.setGender(user.getGender());
            current.setName(user.getName());
            current.setPhone(user.getPhone());
            userrepo.save(current);
            return current;
        }
        return null;
    }
    public Optional<User> login(String email, String password){
        Optional<User> user = userrepo.findByEmailAndPassword(email, password);
        if (user.isEmpty()) {
            throw new RuntimeException("Invalid Credentials");
        }
        return user;
    }

}
