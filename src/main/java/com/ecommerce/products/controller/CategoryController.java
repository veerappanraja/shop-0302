package com.ecommerce.products.controller;

import com.ecommerce.model.Category;
import com.ecommerce.products.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.products.dto.CreateCategoryRequest;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category createCategory(@RequestBody CreateCategoryRequest request) {
        return categoryService.createCategory(request.getName(), request.getDescription());
    }

    @GetMapping
    public List<Category> listCategories() {
        return categoryService.listCategories();
    }
}
