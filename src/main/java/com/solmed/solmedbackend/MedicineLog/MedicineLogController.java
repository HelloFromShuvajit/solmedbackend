package com.solmed.solmedbackend.MedicineLog;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/medicineLog")
public class MedicineLogController {
    
    @Autowired
    private MedicineLogService medlogService;
    
    @GetMapping("/{id}")
    public MedicineLog getMedicinelogById(@PathVariable Long id) {
        return medlogService.getMedicinelogById(id);
    }
    @GetMapping("/userMedicine/{Id}")
    public ResponseEntity<?> getMedicinesByUserId(@PathVariable Long Id) {
        try{
            List<MedicineLog> medicinelogs= medlogService.getMedicinelogsByUserMedicineId(Id);
            return ResponseEntity.ok(medicinelogs);
        }
        catch (RuntimeException e){ 
            return ResponseEntity.status(404).body("No medicinelogs found under this user.");
        }
    }

    @PostMapping("/add")
    public MedicineLog addMedicineLog(@RequestBody MedicineLog medLog) {
        return medlogService.addMedicineLog(medLog);
    }

    @PatchMapping("/medTaken/{id}")
    public MedicineLogRequestDto updatMedicineLogAfterTakenById(@PathVariable Long id){
        return medlogService.updateMedicineLogAfterTakenById(id);
    }
    @PatchMapping("/refill/{id}")
    public MedicineLogRequestDto updatMedicineLogRefillById(@PathVariable Long id, @RequestBody int newStock){
        return medlogService.updateMedicineLogRefillById(id, newStock);
    }
    
    @DeleteMapping("/{id}")
    public void deleteMedicineLogById(@PathVariable Long id){
        medlogService.deleteMedicineLogById(id);
    }
    
}
