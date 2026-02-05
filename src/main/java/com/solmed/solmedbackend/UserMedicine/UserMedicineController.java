package com.solmed.solmedbackend.UserMedicine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/userMedicine")
public class UserMedicineController {

    
    @Autowired
    private UserMedicineService userMedicineService;

    @GetMapping("/{id}")
    public UserMedicine getUserMedicineName(@PathVariable Long id){
        return userMedicineService.getUserMedicineName(id);
    } 

    @PostMapping("/add")
    public UserMedicine addUserMedicine(@RequestBody UserMedicineRequestDto userMedDto) {
    return userMedicineService.addUserMedicine(userMedDto);
    }

    @PatchMapping("/medicine/{id}")
    public UserMedicine updatemedicineById(@PathVariable Long id,@RequestBody Long medId ){
        return userMedicineService.updateMedicineUserMedById(medId, id);

    }

    @DeleteMapping("/{id}")
    public void deleteUserMedicineById(@PathVariable Long id){
        userMedicineService.deleteUserMedicineById(id);
    }

}
