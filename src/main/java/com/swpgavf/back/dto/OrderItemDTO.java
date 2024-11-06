package com.swpgavf.back.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;  // The ID of the OrderItem (can be auto-generated)
    private Long productId;  // The ID of the associated product
    private String sku;
    private String name;
    private String description;
    private Integer quantity;  // Quantity of this product in the order
    private Double price;  // Price of this product at the time of the order
}
