package com.swpgavf.back.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequestDTO {

    private LocalDate orderDate;
    private String status;

    // Update to hold a list of ProductOrderItemDTO (productId + quantity)
    private List<OrderItemDTO> products; // List of products and quantities

    private Long amount; // Amount in cents
    private String currency; // e.g., "usd"
}
