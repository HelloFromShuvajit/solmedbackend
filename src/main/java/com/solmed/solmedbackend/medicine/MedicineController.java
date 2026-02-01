package com.solmed.solmedbackend.medicine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
public class MedicineController {
    
    @Autowired
    private MedicineService medicineService;

    @PostMapping("/addMedicine")
    public Medicine postMedicine(@RequestBody Medicine medicine) {
        return medicineService.addMedicine(medicine);
    }
    
    @GetMapping("/getAllMedicine")
    public Iterable<Medicine> getAllMedicine() {
        return medicineService.getAllMedicine();
    }
    

    @GetMapping("/getMedicineById/{id}")
    public Medicine getMedicineById(@PathVariable Long id) {
        return medicineService.getMedicineById(id);
    }
    
    @DeleteMapping("/deleteMedicineById/{id}")
    public void deleteMedicineById(@PathVariable Long id){
        medicineService.deleteMedicineById(id);
    }
}
