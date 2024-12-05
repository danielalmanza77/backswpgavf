package com.swpgavf.back.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KardexRequestDTO {
    private String employeeName;
    private int quantity;
    private String typeOfMovement;
    private String typeOfOperation;
    private LocalDateTime createdAt;
}
