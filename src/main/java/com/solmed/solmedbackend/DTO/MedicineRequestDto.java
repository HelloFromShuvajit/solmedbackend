package com.solmed.solmedbackend.DTO;

public class MedicineRequestDto {
    private Long medId;



    public MedicineRequestDto(Long medId){
        this.medId = medId;
    }

    public Long getMedId(){
        return medId;
    }
}
