package com.swpgavf.back.dto;

import com.swpgavf.back.entity.Product;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long id;

    private LocalDate orderDate;

    private String status;

    private List<Product> products;
}
