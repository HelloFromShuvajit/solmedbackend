package com.solmed.solmedbackend.UserMedicine;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solmed.solmedbackend.medicine.Medicine;
import com.solmed.solmedbackend.medicine.MedicineRepository;
import com.solmed.solmedbackend.user.User;
import com.solmed.solmedbackend.user.UserRepository;

@Service
public class UserMedicineService {

    @Autowired
    private UserMedicineRepository userMedicineRepo;
    
    @Autowired 
    private UserRepository userrepo;

    @Autowired
    private MedicineRepository medrepo;


    public UserMedicine getUserMedicineName(Long id) {
        return userMedicineRepo.findById(id).orElse(null);
    }
    public UserMedicine addUserMedicine(UserMedicineRequestDto userMedicineDto) {
        UserMedicine userMedicine= new UserMedicine();
        User user = userrepo.findById(userMedicineDto.getUserId()).orElse(null);
        Medicine medicine = medrepo.findById(userMedicineDto.getMedicineId()).orElse(null);
        userMedicine.setUser(user);
        userMedicine.setMedicine(medicine);
        userMedicine.setMedTiming(userMedicineDto.getInputTime());
        
        return userMedicineRepo.save(userMedicine);
    }

    public void deleteUserMedicineById(Long id) {
        userMedicineRepo.deleteById(id);
    }
    public UserMedicine updateMedicineUserMedById(Long medId, Long userMedicineId) {
        UserMedicine userMedicine= userMedicineRepo.findById(userMedicineId).orElse(null);
        if (userMedicine!=null) {
            Medicine medicine = medrepo.findById(medId).orElse(null);
            userMedicine.setMedicine(medicine);
            return userMedicineRepo.save(userMedicine);
        }
        return null;
    }

}
