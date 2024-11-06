package com.swpgavf.back.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductOrderItemDTO {
    private Long Id;
    private String sku;
    private String name;
    private String description;
    private String category;
    private Integer stock;
    private Double price;
    private String brand;
    private List<String> imageUrls;
    private Boolean available;
    private Integer quantity; // Quantity of this product in the order
}
