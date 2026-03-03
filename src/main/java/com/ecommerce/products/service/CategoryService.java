package com.ecommerce.products.service;

import com.ecommerce.model.Category;
import com.ecommerce.products.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category createCategory(String name, String description) {
        Category category = new Category(name, description);
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }
}
