package com.solmed.solmedbackend.MedicineLog;

import com.solmed.solmedbackend.UserMedicine.UserMedicine;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "medicinelog")
public class MedicineLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long MedLogId;

    @OneToOne
    @JoinColumn(name = "userMedicine")
    private UserMedicine userMed;
    

    @Column(name = "medStock", nullable = false, length = 4 )
    private int medStock;


    public Long getMedLogId() {
        return MedLogId;
    }


    public UserMedicine getUserMed() {
        return userMed;
    }


    public int getMedStock() {
        return medStock;
    }


    
    public void setUserMed(UserMedicine userMed) {
        this.userMed = userMed;
    }


    public void setMedStock(int medStock) {
        this.medStock = medStock;
    }

    
    
}
