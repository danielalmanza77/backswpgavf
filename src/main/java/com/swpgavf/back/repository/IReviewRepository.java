package com.swpgavf.back.repository;

import com.swpgavf.back.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProductId(Long productId);
}
