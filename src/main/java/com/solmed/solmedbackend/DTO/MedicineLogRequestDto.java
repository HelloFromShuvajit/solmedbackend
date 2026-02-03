package com.solmed.solmedbackend.DTO;

public class MedicineLogRequestDto {
    private Long medLogId;
    private String message;

    public Long getMedLogId(){
        return medLogId;
    }

    public void setMedLogId(Long id){
        this.medLogId=id;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message= message;
    }

}
