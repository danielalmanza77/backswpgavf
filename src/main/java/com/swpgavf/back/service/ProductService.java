package com.swpgavf.back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swpgavf.back.dto.ProductRequestDTO;
import com.swpgavf.back.dto.ProductResponseDTO;
import com.swpgavf.back.entity.Product;
import com.swpgavf.back.exception.NotFoundException;
import com.swpgavf.back.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService{

    public static final String MESSAGE = "Product not found";
    private final IProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final S3Service s3Service; // Inject S3Service

    public ProductService(IProductRepository productRepository, ObjectMapper objectMapper, S3Service s3Service) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
        this.s3Service = s3Service;
    }

    @Override
    public ProductResponseDTO create(ProductRequestDTO productRequestDTO) {
        Product product = mapToEntity(productRequestDTO);

        // Map image paths or names to pre-signed URLs
        List<String> presignedUrls = generatePresignedUrls(productRequestDTO.getImageUrls());
        product.setImageUrls(presignedUrls);

        // Save product and return the response
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

    public List<String> generatePresignedUrls(List<String> imagePaths) {
        return imagePaths.stream()
                .map(s3Service::generatePresignedUrl) // Generate a presigned URL for each path
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getAll() {
        //return entities transform them into dtos
        return productRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
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
