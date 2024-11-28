package com.swpgavf.back.dto;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Long userId;
    private String userName;
    private String userLastname;
    private String comment;
    private Integer rating;
}
