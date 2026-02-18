package com.solmed.solmedbackend.MedicineLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class MedicineLogService {

    @Autowired
    private MedicineLogRepository medlogRepo;

    public MedicineLog getMedicinelogById(Long id) {
        return medlogRepo.findById(id).orElse(null);
    }

    public MedicineLog addMedicineLog(MedicineLog medLog) {
        return medlogRepo.save(medLog);
    }

    public void deleteMedicineLogById(Long id) {
        medlogRepo.deleteById(id);   
    }

    public MedicineLogRequestDto updateMedicineLogAfterTakenById(Long id) {
        MedicineLog medlog= medlogRepo.findById(id).orElse(null);
        MedicineLogRequestDto medLogDto= new MedicineLogRequestDto();
        if ((medlog!= null)) {
            medLogDto.setMedLogId(id);
            int medStock=medlog.getMedStock();
            if(medStock==0){
                medLogDto.setMessage("Your medicine stock is empty. Cannot register the medicine as taken. Please refill now!!");
                return medLogDto;
            }else{
                
                medStock = medStock-1;
                medLogDto.setMessage("You have "+ medStock + " in you stock.");
                if(medStock<5){
                medLogDto.setMessage("You medicine is low on stock, kindly refill soon. Remaining : "+ medStock);
                }
                medlog.setMedStock(medStock);
                medlogRepo.save(medlog);
                return medLogDto;
            }
        }
        return null;
    }

    public MedicineLogRequestDto updateMedicineLogRefillById(Long id,int newStock) {
        
        MedicineLog medlog = medlogRepo.findById(id).orElse(null);
        MedicineLogRequestDto medLogDto= new MedicineLogRequestDto();
        if (medlog!=null) {
            medLogDto.setMedLogId(id);
            int medStock= medlog.getMedStock();
            medStock+= newStock;
            medlog.setMedStock(medStock);
            medlogRepo.save(medlog);
            medLogDto.setMessage("Your medicine stock has been updated, new stock :" + medStock);
            return medLogDto;
        }
        return null;
    }

    public List<MedicineLog> getMedicinelogsByUserMedicineId(Long userMedicineID) {
        return medlogRepo.findByUserMedId(userMedicineID);
    }
    
}
