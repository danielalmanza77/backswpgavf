package com.swpgavf.back.controller;

import com.swpgavf.back.dto.ReviewRequestDTO;
import com.swpgavf.back.dto.ReviewResponseDTO;
import com.swpgavf.back.service.IReviewService;
import com.swpgavf.back.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final IReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(reviewRequestDTO));
    }
}
