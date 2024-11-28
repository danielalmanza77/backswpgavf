package com.swpgavf.back.service;

import com.swpgavf.back.dto.ReviewRequestDTO;
import com.swpgavf.back.dto.ReviewResponseDTO;
import com.swpgavf.back.entity.Product;
import com.swpgavf.back.entity.Review;
import com.swpgavf.back.entity.User;
import com.swpgavf.back.repository.IReviewRepository;
import com.swpgavf.back.repository.IProductRepository;
import com.swpgavf.back.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    public ReviewService(IReviewRepository reviewRepository, IProductRepository productRepository, IUserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ReviewResponseDTO create(ReviewRequestDTO reviewRequestDTO) {
        // Map the DTO to the entity
        Review review = mapToEntity(reviewRequestDTO);

        // Save the review entity in the database
        Review savedReview = reviewRepository.save(review);

        // Map the saved entity back to the DTO for the response
        return mapToDTO(savedReview);
    }

    @Override
    public List<ReviewResponseDTO> getReviewsOfProductByProductId(Long productId) {
        // Fetch all reviews for the given product ID from the repository
        List<Review> reviews = reviewRepository.findAllByProductId(productId);

        // Map the list of Review entities to a list of ReviewResponseDTOs
        return reviews.stream()
                .map(this::mapToDTO)
                .toList();
    }

    // Mapping the request DTO to the entity
    private Review mapToEntity(ReviewRequestDTO reviewRequestDTO) {
        // Fetch the related product and user from the database
        Product product = productRepository.findById(reviewRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User user = userRepository.findById(reviewRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create and populate the review entity
        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setComment(reviewRequestDTO.getComment());
        review.setRating(reviewRequestDTO.getRating());

        return review;
    }

    // Mapping the review entity to the response DTO
    private ReviewResponseDTO mapToDTO(Review review) {
        // Map the review to the response DTO
        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setId(review.getId());
        responseDTO.setProductId(review.getProduct().getId());
        responseDTO.setProductName(review.getProduct().getName());  // Assuming Product has a `getName()` method
        responseDTO.setUserId(review.getUser().getId());
        responseDTO.setUserName(review.getUser().getName());  // Assuming User has `getFirstName()` and `getLastName()` methods
        responseDTO.setUserLastname(review.getUser().getLastname());
        responseDTO.setComment(review.getComment());
        responseDTO.setRating(review.getRating());

        return responseDTO;
    }
}
