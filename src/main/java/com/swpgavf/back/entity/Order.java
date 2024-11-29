package com.swpgavf.back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private LocalDate orderDate;

    private String status;

    // Updated: Use OrderItems instead of a direct Many-to-Many relationship with Product
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;  // List of order items, each with quantity and product

    private Long amount;  // Total amount in cents (or smallest currency unit)
    private String currency;  // Currency code (e.g., "usd")

    // Method to calculate the total amount based on order items (product price * quantity)
    public void calculateTotalAmount() {
        this.amount = orderItems.stream()
                .mapToLong(item -> (long) (item.getProduct().getPrice() * item.getQuantity() * 100))  // Price in cents
                .sum();
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", amount=" + amount + ", orderDate=" + orderDate + "}";
    }
}
