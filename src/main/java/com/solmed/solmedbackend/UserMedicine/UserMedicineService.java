package com.solmed.solmedbackend.UserMedicine;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solmed.solmedbackend.DTO.UserMedicineRequestDto;
import com.solmed.solmedbackend.medicine.Medicine;
import com.solmed.solmedbackend.medicine.MedicineRepository;
import com.solmed.solmedbackend.user.User;
import com.solmed.solmedbackend.user.UserRepository;

@Service
public class UserMedicineService {

    @Autowired
    private UserMedicineRepository userMedicineRepository;
    
    @Autowired 
    private UserRepository userrepo;

    @Autowired
    private MedicineRepository medrepo;


    public UserMedicine getUserMedicineName(Long id) {
        return userMedicineRepository.findById(id).orElse(null);
    }

    public UserMedicine addUserMedicine(UserMedicineRequestDto userMedicineDto) {
        UserMedicine userMedicine= new UserMedicine();
        User user = userrepo.findById(userMedicineDto.getUserId()).orElse(null);
        Medicine medicine = medrepo.findById(userMedicineDto.getMedicineId()).orElse(null);
        userMedicine.setUser(user);
        userMedicine.setMedicine(medicine);
        userMedicine.setMedTiming(userMedicineDto.getInputTime());
        
        return userMedicineRepository.save(userMedicine);
    }

}
