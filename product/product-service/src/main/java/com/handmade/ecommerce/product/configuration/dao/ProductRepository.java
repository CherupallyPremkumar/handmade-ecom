package com.handmade.ecommerce.product.configuration.dao;

import com.handmade.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  
public interface ProductRepository extends JpaRepository<Product, String> {
    
    /**
     * Find products by category ID
     * 
     * @param categoryId The category ID
     * @return List of products in the category
     */
    List<Product> findByCategoryId(String categoryId);
    
    /**
     * Find products by artisan ID
     * 
     * @param artisanId The artisan ID
     * @return List of products made by the artisan
     */
    List<Product> findByArtisanId(String artisanId);
    
    /**
     * Find products by material
     * 
     * @param material The material
     * @return List of products made of the specified material
     */
    List<Product> findByMaterial(String material);
    
    /**
     * Find products by color
     * 
     * @param color The color
     * @return List of products with the specified color
     */
    List<Product> findByColor(String color);
    
    /**
     * Find products by origin
     * 
     * @param origin The origin location
     * @return List of products from the specified origin
     */
    List<Product> findByOrigin(String origin);
}
