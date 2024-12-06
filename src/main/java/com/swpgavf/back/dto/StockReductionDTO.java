package com.swpgavf.back.dto;

import lombok.Data;

@Data
public class StockReductionDTO {
    private Long productId;
    private int quantity;
}
