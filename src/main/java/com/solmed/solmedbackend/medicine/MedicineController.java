package com.solmed.solmedbackend.medicine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;




@RestController
@RequestMapping("/medicine")
public class MedicineController {
    
    @Autowired
    private MedicineService medicineService;

    @PostMapping("/add")
    public Medicine postMedicine(@RequestBody Medicine medicine) {
        return medicineService.addMedicine(medicine);
    }
    
    @GetMapping("/list")
    public Iterable<Medicine> getAllMedicine() {
        return medicineService.getAllMedicine();
    }
    

    @GetMapping("/{id}")
    public Medicine getMedicineById(@PathVariable Long id) {
        return medicineService.getMedicineById(id);
    }
    
    @DeleteMapping("/{id}")
    public void deleteMedicineById(@PathVariable Long id){
        medicineService.deleteMedicineById(id);
    }
}
