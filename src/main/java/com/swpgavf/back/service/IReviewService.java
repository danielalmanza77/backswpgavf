package com.swpgavf.back.service;

import com.swpgavf.back.dto.ReviewRequestDTO;
import com.swpgavf.back.dto.ReviewResponseDTO;

import java.util.List;

public interface IReviewService {
    ReviewResponseDTO create(ReviewRequestDTO reviewRequestDTO);

    List<ReviewResponseDTO> getReviewsOfProductByProductId(Long id);
}
