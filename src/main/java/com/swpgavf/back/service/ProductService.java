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
        // Map the request DTO to the Product entity
        Product product = mapToEntity(productRequestDTO);

        // Debugging log: Check the image paths (keys) that were passed in
        System.out.println("Received image paths (keys): " + productRequestDTO.getImageUrls());

        // Generate presigned URLs based on the image keys
        List<String> presignedUrls = generatePresignedUrls(productRequestDTO.getImageUrls());

        // Log the generated presigned URLs for debugging purposes
        System.out.println("Generated pre-signed URLs: " + presignedUrls);

        // Store the image keys in the product (not the presigned URLs)
        product.setImageUrls(productRequestDTO.getImageUrls());

        // Log the product object before saving it
        System.out.println("Product to save: " + product);

        // Save the product
        productRepository.save(product);

        // Return the mapped ProductResponseDTO
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

    public void updateAvailability(Long id, boolean available) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        product.setAvailable(available);
        productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        getById(id);
        productRepository.deleteById(id);
    }

    @Override
    public void toggleAvailability(Long id) {
        // Get the product by id
        Product product = getProductEntityById(id);

        if (product != null) {
            // Toggle the 'available' field (true -> false, false -> true)
            product.setAvailable(!product.getAvailable());

            // Save the updated product
            updateAvailability(id, product.getAvailable());
        }
    }


    private ProductResponseDTO mapToDTO(Product product) {
        return objectMapper.convertValue(product, ProductResponseDTO.class);
    }

    private Product mapToEntity(ProductRequestDTO productRequestDTO) {
        Product product = objectMapper.convertValue(productRequestDTO, Product.class);
        // Set default availability to true if null
        if (product.getAvailable() == null) {
            product.setAvailable(true);
        }
        return product;
    }
}
