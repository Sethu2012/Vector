package com.taskmanagement.service;

import com.taskmanagement.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {

    private final String BACKEND_API_URL = "http://localhost:8083/api/categories";

    @Autowired
    private RestTemplate restTemplate;

    public List<Category> getAllCategories() {
        Category[] categoriesArray = restTemplate.getForObject(BACKEND_API_URL, Category[].class);
        return Arrays.asList(categoriesArray != null ? categoriesArray : new Category[0]);
    }

    public Category addCategory(Category category) {
        return restTemplate.postForObject(BACKEND_API_URL, category, Category.class);
    }

    public Category getCategoryById(Long id) {
        return restTemplate.getForObject(BACKEND_API_URL + "/" + id, Category.class);
    }

    public void deleteCategory(Long id) {
        String deleteUrl = BACKEND_API_URL + "/" + id;
        restTemplate.delete(deleteUrl);
    }
}
