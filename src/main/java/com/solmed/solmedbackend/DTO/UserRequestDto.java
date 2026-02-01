package com.solmed.solmedbackend.DTO;

public class UserRequestDto {
    private String name;
    private String email;
    private Long id;
    
    
    public UserRequestDto(Long id){
        this.id = id; 
        }
    
    
    public Long getId() {
        return id;
    }
}
