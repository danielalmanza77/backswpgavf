package com.swpgavf.back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swpgavf.back.dto.ProductRequestDTO;
import com.swpgavf.back.dto.ProductResponseDTO;
import com.swpgavf.back.entity.Product;
import com.swpgavf.back.exception.NotFoundException;
import com.swpgavf.back.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService{

    public static final String MESSAGE = "Product not found";
    private final IProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public ProductService(IProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public ProductResponseDTO create(ProductRequestDTO productRequestDTO) {
        Product product = mapToEntity(productRequestDTO);
        productRepository.save(product);
        return mapToDTO(product);
    }

    @Override
    public ProductResponseDTO getById(Long id) {
        //receives an id, finds and returns as dto
        Product product = productRepository.findById(id).orElseThrow
                (() -> new NotFoundException(MESSAGE));
        return mapToDTO(product);
    }

    /**
     * Method used in Order Service because it needs a Product not a DTO
     * @param id
     * @return
     */
    public Product getProductEntityById(Long id) {
        // Retrieves the Product entity directly, throws exception if not found
        return productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product not found with ID: " + id));
    }

    /**
     * Get products (plural) by Ids
     * @param productIds
     * @return
     */
    public List<Product> getProductsEntityByIds(List<Long> productIds) {
        return productRepository.findAllById(productIds); // This method fetches products by their IDs
    }

    private ProductResponseDTO mapToDTO(Product product) {
        return objectMapper.convertValue(product, ProductResponseDTO.class);
    }

    private Product mapToEntity(ProductRequestDTO productRequestDTO) {
        return objectMapper.convertValue(productRequestDTO, Product.class);
    }
}
