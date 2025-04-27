package com.handmade.ecommerce.product.service.impl;

import com.handmade.ecommerce.price.dto.CreatePriceRequestDTO;


import com.handmade.ecommerce.product.configuration.dao.ProductPriceRepository;
import com.handmade.ecommerce.product.configuration.dao.ProductRepository;
import com.handmade.ecommerce.product.model.Product;
import com.handmade.ecommerce.product.model.ProductPrice;
import com.handmade.ecommerce.product.model.service.ProductService;
import org.chenile.base.exception.NotFoundException;
import org.chenile.stm.STM;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl extends StateEntityServiceImpl<Product> implements ProductService {

    @Qualifier("_priceService_")
    @Autowired
    private PriceS priceService;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductPriceRepository productPriceRepository;

    public ProductServiceImpl(STM<Product> stm, @Qualifier("productActionsInfoProvider") STMActionsInfoProvider stmActionsInfoProvider, EntityStore<Product> entityStore) {
        super(stm, stmActionsInfoProvider, entityStore);
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        StateEntityServiceResponse<Product> serviceResponse= super.create(product);
        return serviceResponse.getMutatedEntity();
    }
    
    @Override
    @Transactional
    public Product updateProduct(Product product) {
        // Verify the product exists before updating
        if (!productRepository.existsById(product.getId())) {
            throw new NotFoundException(1500, "Unable to find Product with ID " + product.getId());
        }
        return productRepository.save(product);
    }
    
    @Override
    public Optional<Product> findProductById(String id) {
        return productRepository.findById(id);
    }
    

    
    @Override
    public List<Product> findProductsByCategory(String categoryId) {
        // Custom query to find products by category
        return productRepository.findByCategoryId(categoryId);
    }
    
    @Override
    public List<Product> findProductsByArtisan(String artisanId) {
        // Custom query to find products by artisan
        return productRepository.findByArtisanId(artisanId);
    }
    
    @Override
    public List<Product> findProductsByMaterial(String material) {
        // Custom query to find products by material
        return productRepository.findByMaterial(material);
    }
    
    @Override
    @Transactional
    public ProductPrice addProductPrice(CreatePriceRequestDTO createPriceRequestDTO) {
        // Find the product
        Product product = productRepository.findById(createPriceRequestDTO.getProductId())
                .orElseThrow(() -> new NotFoundException(1500, "Unable to find Product with ID " + createPriceRequestDTO.getProductId()));
        priceService
        return productPriceRepository.save(productPrice);
    }
    
    @Override
    @Transactional
    public ProductPrice updateProductPrice(String priceId, BigDecimal newPrice) {
        ProductPrice price = productPriceRepository.findById(priceId)
                .orElseThrow(() -> new NotFoundException(1501, "Unable to find Product Price with ID " + priceId));
        
        price.setPrice(newPrice);
        return productPriceRepository.save(price);
    }
    
    @Override
    public List<ProductPrice> getProductPrices(String productId) {
        return productPriceRepository.findByProductId(productId);
    }
    
    @Override
    public Optional<ProductPrice> getProductPrice(String productId, String locationCode, String currencyCode) {
        ProductPrice price = productPriceRepository.findByProductIdAndLocationCodeAndCurrencyCode(
                productId, locationCode, currencyCode);
        return Optional.ofNullable(price);
    }
} 