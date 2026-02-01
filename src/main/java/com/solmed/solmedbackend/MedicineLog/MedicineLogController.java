package com.solmed.solmedbackend.MedicineLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class MedicineLogController {
    
    @Autowired
    private MedicineLogService medlogService;
    
    @GetMapping("/getMedicineLogById/{id}")
    public MedicineLog getMedicinelogById(@PathVariable Long id) {
        return medlogService.getMedicinelogById(id);
    }

    @PostMapping("/addMedicineLog")
    public MedicineLog addMedicineLog(@RequestBody MedicineLog medLog) {
        return medlogService.addMedicineLog(medLog);
    }

    @PatchMapping("/updateMedicineLogAfterTakenById/{id}")
    public MedicineLog updatMedicineLogAfterTakenById(@PathVariable Long id){
        return medlogService.updateMedicineLogAfterTakenById(id);
    }
    
    @DeleteMapping("/deleteMedicineLogById/{id}")
    public void deleteMedicineLogById(@PathVariable Long id){
        medlogService.deleteMedicineLogById(id);
    }
    
}
