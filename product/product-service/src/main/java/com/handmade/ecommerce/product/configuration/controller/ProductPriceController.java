package com.handmade.ecommerce.product.configuration.controller;

import com.handmade.ecommerce.product.model.ProductPrice;
import com.handmade.ecommerce.product.model.service.ProductService;
import org.chenile.base.http.GenericResponse;
import org.chenile.security.api.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/product-prices")
public class ProductPriceController {

    @Autowired
    private ProductService productService;
    
    /**
     * Get all prices for a product
     * 
     * @param productId The product ID
     * @return List of prices for the product
     */
    @GetMapping("/product/{productId}")
    @SecurityConfig(authorities = {"product.read", "test.premium"})
    public ResponseEntity<GenericResponse<List<ProductPrice>>> getProductPrices(@PathVariable String productId) {
        List<ProductPrice> prices = productService.getProductPrices(productId);
        GenericResponse<List<ProductPrice>> response = new GenericResponse<>();
        response.setData(prices);
        response.setStatus(0);
        response.setMessage("Success");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get a specific price for a product by location and currency
     * 
     * @param productId The product ID
     * @param locationCode The location code
     * @param currencyCode The currency code
     * @return The matching product price
     */
    @GetMapping("/product/{productId}/location/{locationCode}/currency/{currencyCode}")
    @SecurityConfig(authorities = {"product.read", "test.premium"})
    public ResponseEntity<GenericResponse<ProductPrice>> getProductPrice(
            @PathVariable String productId,
            @PathVariable String locationCode,
            @PathVariable String currencyCode) {
        
        Optional<ProductPrice> priceOpt = productService.getProductPrice(productId, locationCode, currencyCode);
        
        GenericResponse<ProductPrice> response = new GenericResponse<>();
        if (priceOpt.isPresent()) {
            response.setData(priceOpt.get());
            response.setStatus(0);
            response.setMessage("Success");
            return ResponseEntity.ok(response);
        } else {
            response.setStatus(1);
            response.setMessage("Price not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * Add a new price for a product
     * 
     * @param productId The product ID
     * @param priceData The price data with location, currency and price
     * @return The created price
     */
    @PostMapping("/product/{productId}")
    @SecurityConfig(authorities = {"product.write", "test.premium"})
    public ResponseEntity<GenericResponse<ProductPrice>> addProductPrice(
            @PathVariable String productId,
            @RequestBody Map<String, Object> priceData) {
        
        String locationCode = (String) priceData.get("locationCode");
        String currencyCode = (String) priceData.get("currencyCode");
        BigDecimal price = new BigDecimal(priceData.get("price").toString());
        
        ProductPrice newPrice = productService.addProductPrice();
        
        GenericResponse<ProductPrice> response = new GenericResponse<>();
        response.setData(newPrice);
        response.setStatus(0);
        response.setMessage("Price added successfully");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Update an existing price
     * 
     * @param priceId The price ID
     * @param priceData The updated price data
     * @return The updated price
     */
    @PutMapping("/{priceId}")
    @SecurityConfig(authorities = {"product.write", "test.premium"})
    public ResponseEntity<GenericResponse<ProductPrice>> updateProductPrice(
            @PathVariable String priceId,
            @RequestBody Map<String, Object> priceData) {
        
        BigDecimal newPrice = new BigDecimal(priceData.get("price").toString());
        
        ProductPrice updatedPrice = productService.updateProductPrice(priceId, newPrice);
        
        GenericResponse<ProductPrice> response = new GenericResponse<>();
        response.setData(updatedPrice);
        response.setStatus(0);
        response.setMessage("Price updated successfully");
        
        return ResponseEntity.ok(response);
    }
} 