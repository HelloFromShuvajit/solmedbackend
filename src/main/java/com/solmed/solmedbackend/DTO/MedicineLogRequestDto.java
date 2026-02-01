package com.solmed.solmedbackend.DTO;

public class MedicineLogRequestDto {
    private Long userMedId;

    private int medStock;

    public int getMedStock(){
        return medStock;
    }


    public void setMedStock(int medStock) {
        this.medStock = medStock;
    }


    public Long getUserMedId(){
        return userMedId;
    }

}
