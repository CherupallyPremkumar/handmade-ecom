//package com.handmade.ecommerce.price.client;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.http.ResponseEntity;
//
//@FeignClient(name = "product-service", path = "/api/products")
//public interface ProductClient {
//
//    /**
//     * Check if a product exists
//     * @param productId the ID of the product to check
//     * @return ResponseEntity with status 200 if product exists, 404 if not
//     */
//    @GetMapping("/{productId}/exists")
//    ResponseEntity<Void> checkProductExists(@PathVariable String productId);
//}