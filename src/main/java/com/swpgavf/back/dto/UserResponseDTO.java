package com.swpgavf.back.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponseDTO {

    private Long id;
    private String type;
    private String username;
    private String password;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private LocalDate dob;

}
