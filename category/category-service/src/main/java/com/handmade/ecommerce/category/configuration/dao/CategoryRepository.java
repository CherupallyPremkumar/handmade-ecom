package com.handmade.ecommerce.category.configuration.dao;

import com.handmade.ecommerce.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface CategoryRepository extends JpaRepository<Category,String> {}
