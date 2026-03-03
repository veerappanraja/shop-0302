package com.ecommerce.products.service;

import com.ecommerce.config.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.model.Inventory;
import com.ecommerce.model.Product;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.products.repository.CategoryRepository;
import com.ecommerce.products.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public Product createProduct(String name, String description, BigDecimal price, Long categoryId) {
        if (categoryId != null) {
            categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        Product product = new Product(name, description, price, categoryId);
        product = productRepository.save(product);

        // Initialize inventory for new product
        Inventory inventory = new Inventory(product.getId(), 0, 0);
        inventoryRepository.save(inventory);

        return product;
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Transactional(readOnly = true)
    public List<Product> listProducts() {
        return productRepository.findAll();
    }
}
