package com.swpgavf.back.dto;

import com.swpgavf.back.entity.Product;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequestDTO {

    private LocalDate orderDate;

    private String status;

    private List<Long> productIds;

    private Long amount; // Amount in cents

    private String currency; // e.g., "usd"
}
