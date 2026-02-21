package com.solmed.solmedbackend.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name ="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable =false, unique = true)
    private String email;

    @Column(nullable = false, length = 10)
    private String phone;

    // need to add password with security in future

    @Column(nullable = false, length = 100)
    private String password;
    
    @Column(nullable = false, length = 3)
    private Integer age;
    
    @Column(nullable = false, length = 10)
    private String gender;

    @Column(nullable = false, length = 10)
    private String position;
}
