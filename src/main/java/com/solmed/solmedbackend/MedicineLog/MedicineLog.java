package com.solmed.solmedbackend.MedicineLog;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "medicinelog")
public class MedicineLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long MedLogId;

    
    @Column(name = "userMedicineId")
    private Long userMedId;
    

    @Column(name = "medStock", nullable = false, length = 4 )
    private int medStock;
}
