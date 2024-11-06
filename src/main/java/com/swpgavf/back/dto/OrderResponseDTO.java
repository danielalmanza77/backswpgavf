package com.swpgavf.back.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private LocalDate orderDate;
    private String status;
    private List<ProductOrderItemDTO> products;
    private Long amount;
    private String currency;
}
