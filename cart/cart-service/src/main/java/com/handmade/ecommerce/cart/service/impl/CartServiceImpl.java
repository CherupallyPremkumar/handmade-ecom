package com.handmade.ecommerce.cart.service.impl;

import com.handmade.ecommerce.cart.dto.AddItemRequestDTO;
import com.handmade.ecommerce.cart.dto.CartDTO;
import com.handmade.ecommerce.cart.dto.CartItemDTO;
import com.handmade.ecommerce.cart.mapper.CartMapper;
import org.chenile.stm.STM;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.handmade.ecommerce.cart.model.Cart;
import com.handmade.ecommerce.cart.model.CartItem;
import com.handmade.ecommerce.cart.api.CartService;
import com.handmade.ecommerce.cart.configuration.dao.CartRepository;
import com.handmade.ecommerce.cart.service.cmds.CreateOrderFromCartCommand;
import com.handmade.ecommerce.core.exception.BusinessException;
import com.handmade.ecommerce.core.exception.ErrorCode;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl extends StateEntityServiceImpl<Cart> implements CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CreateOrderFromCartCommand createOrderFromCartCommand;

    public CartServiceImpl(STM<Cart> stm, STMActionsInfoProvider cartActionsInfoProvider, EntityStore<Cart> entityStore) {
        super(stm, cartActionsInfoProvider, entityStore);
    }

    @Override
    @Transactional
    public CartDTO createCart(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "User ID cannot be empty");
        }

        // Check if user already has an active cart
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);
        if (existingCart.isPresent() && "ACTIVE".equals(existingCart.get().getStatus())) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, 
                "User already has an active cart");
        }

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setStatus("ACTIVE");
        super.create(cart);
        return cartMapper.toDTO(cart);
    }

    @Override
    public CartDTO getCartByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "User ID cannot be empty");
        }

        return cartMapper.toDTO(cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, 
                    "Cart not found for user: " + userId)));
    }

    @Override
    public List<CartDTO> getAllCarts() {
        return cartMapper.toDTOList(cartRepository.findAll());
    }

    @Override
    @Transactional
    public CartDTO addItemToCart(String cartId, AddItemRequestDTO item) {
        if (cartId == null || cartId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Cart ID cannot be empty");
        }
        if (item == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Item cannot be null");
        }

        Cart cart = super.retrieve(cartId).getMutatedEntity();
        
        // Validate cart status
        if (!"ACTIVE".equals(cart.getStatus())) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, 
                "Cannot add items to a non-active cart");
        }

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(item.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity of existing item
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
            cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getUnitPrice());
        } else {
            // Add new item
            cart.getItems().add(cartMapper.toEntity(item));
        }

        cart.setTotalAmount(calculateCartTotal(cartId));
        super.entityStore.store(cart);
        return cartMapper.toDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO updateCartItem(String cartId, String itemId, int quantity) {
        if (cartId == null || cartId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Cart ID cannot be empty");
        }
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Item ID cannot be empty");
        }
        if (quantity <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Quantity must be greater than zero");
        }

        Cart cart = super.entityStore.retrieve(cartId);
        
        // Validate cart status
        if (!"ACTIVE".equals(cart.getStatus())) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, 
                "Cannot update items in a non-active cart");
        }

        Optional<CartItem> item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst();
        
        if (item.isPresent()) {
            CartItem cartItem = item.get();
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(quantity * cartItem.getUnitPrice());
            cart.setTotalAmount(calculateCartTotal(cartId));
            super.entityStore.store(cart);
            return cartMapper.toDTO(cart);
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "Item not found in cart");
    }

    @Override
    @Transactional
    public CartDTO removeItemFromCart(String cartId, String itemId) {
        if (cartId == null || cartId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Cart ID cannot be empty");
        }
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Item ID cannot be empty");
        }

        Cart cart = super.entityStore.retrieve(cartId);
        
        // Validate cart status
        if (!"ACTIVE".equals(cart.getStatus())) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, 
                "Cannot remove items from a non-active cart");
        }

        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(itemId));
        if (!removed) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Item not found in cart");
        }

        cart.setTotalAmount(calculateCartTotal(cartId));
        super.entityStore.store(cart);
        return cartMapper.toDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO updateCartStatus(String cartId, String status) {
        if (cartId == null || cartId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Cart ID cannot be empty");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Status cannot be empty");
        }

        Cart cart = retrieve(cartId);
        cart.setStatus(status);
        return store(cart);
    }

    @Override
    @Transactional
    public void deleteCart(String cartId) {
        if (cartId == null || cartId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Cart ID cannot be empty");
        }

        Cart cart = retrieve(cartId);
        if (cart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Cart not found");
        }

        // Only allow deletion of non-active carts
        if ("ACTIVE".equals(cart.getStatus())) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, 
                "Cannot delete an active cart");
        }

        cartRepository.deleteById(cartId);
    }

    @Override
    public double calculateCartTotal(String cartId) {
        if (cartId == null || cartId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Cart ID cannot be empty");
        }

        Cart cart = retrieve(cartId);
        if (cart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Cart not found");
        }

        return cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    @Override
    @Transactional
    public Cart createOrderFromCart(String cartId) {
        if (cartId == null || cartId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Cart ID cannot be empty");
        }

        Cart cart = retrieve(cartId);
        if (cart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Cart not found");
        }
        
        // Validate cart can be converted to order
        if (cart.getItems().isEmpty()) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, 
                "Cannot create order from empty cart");
        }
        
        if ("CONVERTED_TO_ORDER".equals(cart.getStatus())) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, 
                "Cart has already been converted to an order");
        }

        if (!"ACTIVE".equals(cart.getStatus())) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, 
                "Only active carts can be converted to orders");
        }
        
        try {
            // Execute the command to create order
            createOrderFromCartCommand.execute(cart);
            return store(cart);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, 
                "Failed to create order from cart: " + e.getMessage());
        }
    }
} 