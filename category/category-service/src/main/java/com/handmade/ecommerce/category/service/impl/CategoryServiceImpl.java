package com.handmade.ecommerce.category.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.handmade.ecommerce.category.model.Category;
import com.handmade.ecommerce.category.service.CategoryService;

import com.handmade.ecommerce.category.configuration.dao.CategoryRepository;
import org.chenile.base.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

public class CategoryServiceImpl implements CategoryService{
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public Category save(Category entity) {
        entity = categoryRepository.save(entity);
        return entity;
    }

    @Override
    public Category retrieve(String id) {
        Optional<Category> entity = categoryRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find category with ID " + id);
    }
}