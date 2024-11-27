package com.swpgavf.back.repository;

import com.swpgavf.back.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, String status);
}
