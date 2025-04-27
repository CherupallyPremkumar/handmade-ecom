package com.handmade.ecommerce.cart.api;


import com.handmade.ecommerce.cart.dto.AddItemRequestDTO;
import com.handmade.ecommerce.cart.dto.CartDTO;
import com.handmade.ecommerce.cart.dto.CartItemDTO;

import java.util.List;

public interface CartService {
    CartDTO createCart(String userId);
    CartDTO getCartByUserId(String userId);
    List<CartDTO> getAllCarts();
    CartDTO addItemToCart(String cartId, AddItemRequestDTO item);
    CartDTO updateCartItem(String cartId, String itemId, int quantity);
    CartDTO removeItemFromCart(String cartId, String itemId);
    CartDTO updateCartStatus(String cartId, String status);
    void deleteCart(String cartId);
    double calculateCartTotal(String cartId);
    CartDTO createOrderFromCart(String cartId);
} 