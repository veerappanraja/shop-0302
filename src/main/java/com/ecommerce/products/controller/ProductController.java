package com.ecommerce.products.controller;

import com.ecommerce.model.Product;
import com.ecommerce.products.dto.CreateProductRequest;
import com.ecommerce.products.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(@RequestBody CreateProductRequest request) {
        return productService.createProduct(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getCategoryId()
        );
    }

    @GetMapping("/{product_id}")
    public Product getProduct(@PathVariable("product_id") Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping
    public List<Product> listProducts() {
        return productService.listProducts();
    }
}
