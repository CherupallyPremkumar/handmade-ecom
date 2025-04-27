package com.handmade.ecommerce.cart.mapper;

import com.handmade.ecommerce.cart.dto.AddItemRequestDTO;
import com.handmade.ecommerce.cart.dto.CartDTO;
import com.handmade.ecommerce.cart.dto.CartItemDTO;
import com.handmade.ecommerce.cart.dto.UpdateItemRequestDTO;
import com.handmade.ecommerce.cart.model.Cart;
import com.handmade.ecommerce.cart.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Cart entities and DTOs
 */
@Component
public class CartMapper {

    /**
     * Convert a Cart entity to a CartDTO
     * @param cart the Cart entity
     * @return the CartDTO
     */
    public CartDTO toDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        List<CartItemDTO> itemDTOs = cart.getItems() != null
                ? cart.getItems().stream().map(this::toDTO).collect(Collectors.toList())
                : null;

        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(itemDTOs)
                .totalAmount(cart.getTotalAmount())
                .status(cart.getStatus())
                .createdAt(cart.getCreatedTime())
                .updatedAt(cart.getLastModifiedTime())
                .tenantId(cart.tenant)
                .build();
    }

    /**
     * Convert a CartItem entity to a CartItemDTO
     * @param cartItem the CartItem entity
     * @return the CartItemDTO
     */
    public CartItemDTO toDTO(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        return CartItemDTO.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProductId())
                .productName(cartItem.getProductName())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .totalPrice(cartItem.getTotalPrice())
                .cartId(cartItem.getCartId())
                .build();
    }

    /**
     * Convert a list of Cart entities to a list of CartDTOs
     * @param carts the list of Cart entities
     * @return the list of CartDTOs
     */
    public List<CartDTO> toDTOList(List<Cart> carts) {
        if (carts == null) {
            return null;
        }

        return carts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new CartItem entity from an AddItemRequestDTO
     * @param dto the AddItemRequestDTO
     * @return the CartItem entity
     */
    public CartItem toEntity(AddItemRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        CartItem cartItem = new CartItem();
        cartItem.setProductId(dto.getProductId());
        cartItem.setProductName(dto.getProductName());
        cartItem.setQuantity(dto.getQuantity());
        cartItem.setUnitPrice(dto.getUnitPrice());
        cartItem.setTotalPrice(dto.getQuantity() * dto.getUnitPrice());

        return cartItem;
    }

    /**
     * Update a CartItem entity with data from an UpdateItemRequestDTO
     * @param cartItem the CartItem entity to update
     * @param dto the UpdateItemRequestDTO
     * @return the updated CartItem entity
     */
    public CartItem updateEntity(CartItem cartItem, UpdateItemRequestDTO dto) {
        if (cartItem == null || dto == null) {
            return cartItem;
        }

        cartItem.setQuantity(dto.getQuantity());
        cartItem.setTotalPrice(dto.getQuantity() * cartItem.getUnitPrice());

        return cartItem;
    }
}
