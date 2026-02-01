package com.solmed.solmedbackend.MedicineLog;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.solmed.solmedbackend.DTO.MedicineLogRequestDto;
import com.solmed.solmedbackend.UserMedicine.UserMedicine;
import com.solmed.solmedbackend.UserMedicine.UserMedicineRepository;

@Service
public class MedicineLogService {

    @Autowired
    private MedicineLogRepository medlogRepo;

    @Autowired
    private UserMedicineRepository userMedRepo;


    public MedicineLog getMedicinelogById(Long id) {
        return medlogRepo.findById(id).orElse(null);
    }

    public MedicineLog addMedicineLog(MedicineLogRequestDto medLogDto) {
        MedicineLog medLog= new MedicineLog();
        UserMedicine userMedicine = userMedRepo.findById(medLogDto.getUserMedId()).orElse(null);
        medLog.setMedStock(medLogDto.getMedStock());
        medLog.setUserMed(userMedicine);
        return medlogRepo.save(medLog);
    }

    public void deleteMedicineLogById(Long id) {
        medlogRepo.deleteById(id);   
    }

    public MedicineLog updateMedicineLogAfterTakenById(Long id) {
        MedicineLog medlog= medlogRepo.findById(id).orElse(null);
        if ((medlog!= null)) {
            int medStock=medlog.getMedStock();
            if(medStock==0){
                System.out.println("The medicine was not available.");
                return null;
            }else{
                
                medStock = medStock-1;
                if(medStock<5){
                    System.out.println("This medicine is about to end, remaining: "+medStock);
                }
                medlog.setMedStock(medStock);
                medlogRepo.save(medlog);
                return medlog;
            }
        }
        return null;


    }
    
}
