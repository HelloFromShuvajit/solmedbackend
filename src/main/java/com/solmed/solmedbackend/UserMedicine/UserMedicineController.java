package com.solmed.solmedbackend.UserMedicine;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.solmed.solmedbackend.DTO.UserMedicineRequestDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class UserMedicineController {

    
    @Autowired
    private UserMedicineService userMedicineService;

    @GetMapping("/getMedNamesOfAUser/{id}")
    public UserMedicine getUserMedicineName(@PathVariable Long id){
        return userMedicineService.getUserMedicineName(id);
    } 

    @PostMapping("/addUserMedicine")
    public UserMedicine addUserMedicine(@RequestBody UserMedicineRequestDto userMedDto) {
        
        return userMedicineService.addUserMedicine(userMedDto);
    }
    @PostMapping("/test2")
    public LocalTime test2(@RequestBody LocalTime body) {
    System.out.println(body);
    return body;
}

}
