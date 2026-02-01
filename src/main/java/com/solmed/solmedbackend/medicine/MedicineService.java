package com.solmed.solmedbackend.medicine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicinerepo;


    public Medicine getMedicineById(Long id){
        return medicinerepo.findById(id).orElse(null);
    }



    public Medicine addMedicine(Medicine medicine) {
        return medicinerepo.save(medicine);
    }



    public Iterable<Medicine> getAllMedicine() {
        return medicinerepo.findAll();
    }



    public void deleteMedicineById(Long id) {
        medicinerepo.deleteById(id);
    }
}
