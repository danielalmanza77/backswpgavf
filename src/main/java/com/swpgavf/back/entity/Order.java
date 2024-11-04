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

    @ManyToMany(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinTable(
            name = "orders_details",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    // New fields
    private Long amount; // Total amount in cents (or smallest currency unit)
    private String currency; // Currency code (e.g., "usd")

    // Method to calculate total amount based on products
    public void calculateTotalAmount() {
        this.amount = products.stream()
                .mapToLong(product -> (long) (product.getPrice() * 100)) // Assuming price is in dollars
                .sum();
    }
}
