package com.example.beginnerfitbe.category.service;

import com.example.beginnerfitbe.category.domain.Category;
import com.example.beginnerfitbe.category.repository.CategoryRepository;
import com.example.beginnerfitbe.post.domain.Post;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    @PostConstruct
    public void initCategories() {
        List<String> categoryNames = Arrays.asList("자유게시판", "정보공유");

        categoryNames.forEach(categoryName -> {
            if (categoryRepository.findByCategoryName(categoryName).isEmpty()) {
                categoryRepository.save(new Category(categoryName));
            }
        });
    }
}
