package com.solmed.solmedbackend.MedicineLog;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medicinelog")
public class MedicineLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long MedLogId;

    
    @Column(name = "userMedicineId")
    private Long userMedId;
    

    @Column(name = "medStock", nullable = false, length = 4 )
    private int medStock;


    public Long getMedLogId() {
        return MedLogId;
    }


    public Long getUserMed() {
        return userMedId;
    }


    public int getMedStock() {
        return medStock;
    }


    
    public void setUserMedId(Long userMedId) {
        this.userMedId = userMedId;
    }


    public void setMedStock(int medStock) {
        this.medStock = medStock;
    }

    
    
}
