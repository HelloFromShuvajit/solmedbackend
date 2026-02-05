package com.solmed.solmedbackend.UserMedicine;

import java.time.LocalTime;


import com.solmed.solmedbackend.medicine.Medicine;
import com.solmed.solmedbackend.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user_medicines")
public class UserMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "medicine")
    private Medicine medicine;
    
    @Column(name = "med_timing")
    private LocalTime medTiming;
}