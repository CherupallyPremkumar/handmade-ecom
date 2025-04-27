package com.handmade.ecommerce.cart.service.cmds;

import org.chenile.stm.State;
import org.chenile.stm.StateEntity;
import org.chenile.stm.action.STMAction;
import org.springframework.stereotype.Component;
import com.handmade.ecommerce.cart.model.Cart;
import com.handmade.ecommerce.cart.model.CartItem;
import com.handmade.ecommerce.cart.dto.AddItemRequestDTO;
import com.handmade.ecommerce.cart.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AddItemToCartCommand implements STMAction<Cart> {
    
    @Autowired
    private CartMapper cartMapper;
    
    @Override
    public void execute(State<Cart> state) {
        Cart cart = state.getEntity();
        AddItemRequestDTO itemRequest = (AddItemRequestDTO) state.getContext().get("itemRequest");
        
        if (itemRequest == null) {
            throw new IllegalArgumentException("Item request cannot be null");
        }
        
        // Check if item already exists in cart
        CartItem existingItem = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(itemRequest.getProductId()))
                .findFirst()
                .orElse(null);
                
        if (existingItem != null) {
            // Update quantity of existing item
            existingItem.setQuantity(existingItem.getQuantity() + itemRequest.getQuantity());
            existingItem.setTotalPrice(existingItem.getQuantity() * existingItem.getUnitPrice());
        } else {
            // Add new item
            cart.getItems().add(cartMapper.toEntity(itemRequest));
        }
        
        // Update cart total
        cart.setTotalAmount(cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum());
    }
} 