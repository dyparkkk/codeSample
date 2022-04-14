package com.example.dddstart.category;

import org.springframework.data.domain.Page;

import java.util.List;

public class ProductListService {

    private final CategoryRepository categoryRepository;

    // 카테고리에 속한 상품 목록을 제공하는 응용 서비스
    public Page<Product> getProductOfCategory(Long categoryId, int page, int size) {
        Category category = categoryRepository.findById(categoryId);
        checkCategory(category);
        List<Product> products = productRepository.findByCategoryId(category.getId(), page, size);
        int totalCount = productRepository.countsByCategoryId(category.getId());
        return new Page(page, size, totalCount, products);
    }
}
