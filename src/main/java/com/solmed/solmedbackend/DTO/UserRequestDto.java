package com.solmed.solmedbackend.DTO;

public class UserRequestDto {
    private Long id;
    private String userName;
    public UserRequestDto(Long id){
        this.id = id; 
            }

        
    public Long getId() {
        return id;
    }
}
