package com.swpgavf.back.dto;

import lombok.Data;

@Data
public class ReviewRequestDTO {
    private Long productId;
    private Long userId;
    private String comment;
    private Integer rating;
}
