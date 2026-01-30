package com.solmed.solmedbackend.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name ="users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable =false, unique = true)
    private String email;

    @Column(nullable = false, length = 10)
    private Long phone;

    @Column(nullable = false, length = 64) 
    private String password;
    
    @Column(nullable = false, length = 3)
    private int age;
    
    @Column(nullable = false, length = 10)
    private String gender;
    
    
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Long getPhone() {
        return phone;
    }
    public void setPhone(Long phone) {
        this.phone = phone;
    }
        
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setId(Long id){             //do I need this?
        this.id = id;
    }
    public Long getId(){
        return id;
    }
     public void setName(String name){
        this.name = name;
     }
     public String getName(){
        return name;
     }
}
