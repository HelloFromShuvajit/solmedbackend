package com.solmed.solmedbackend.medicine;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/addByUser")
    public ResponseEntity<?> addMedicineByUser(@RequestBody Medicine medicine) {
        try {
            Optional<Medicine> newMed = medicineService.addMedicineByUser(medicine);
            return ResponseEntity.status(HttpStatus.CREATED).body(newMed);
            }
            catch(IllegalArgumentException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }        
    }   
}
