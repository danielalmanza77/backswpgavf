package com.swpgavf.back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sku;
    private String name;
    private String description;
    private String category;
    private Integer stock;
    private Double price;
    private String brand;

    @Lob
    @ElementCollection
    private List<String> imageUrls;

    private Boolean available = false;

    @ElementCollection
    private List<Review> reviews;


    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + "}";
    }
}
