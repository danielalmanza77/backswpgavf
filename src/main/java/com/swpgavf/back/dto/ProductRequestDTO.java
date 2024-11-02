package com.swpgavf.back.dto;

import lombok.Data;

@Data
public class ProductRequestDTO {
    private String sku;
    private String name;
    private String description;
    private String category;
    private Integer stock;
    private Double price;
    private String brand;
}
