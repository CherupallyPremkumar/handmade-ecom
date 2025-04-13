package com.handmade.ecommerce.category.service;

import com.handmade.ecommerce.category.model.Category;

public interface CategoryService {
	// Define your interface here
    public Category save(Category category);
    public Category retrieve(String id);
}
