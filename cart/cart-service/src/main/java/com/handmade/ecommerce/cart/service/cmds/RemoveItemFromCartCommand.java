package com.handmade.ecommerce.cart.service.cmds;

import org.chenile.stm.State;
import org.chenile.stm.StateEntity;
import org.chenile.stm.action.STMAction;
import org.springframework.stereotype.Component;
import com.handmade.ecommerce.cart.model.Cart;
import com.handmade.ecommerce.cart.model.CartItem;

@Component
public class RemoveItemFromCartCommand implements STMAction<Cart> {
    
    @Override
    public void execute(State<Cart> state) {
        Cart cart = state.getEntity();
        String itemId = (String) state.getContext().get("itemId");
        
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        
        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(itemId));
        
        if (!removed) {
            throw new IllegalArgumentException("Item not found in cart");
        }
        
        // Update cart total
        cart.setTotalAmount(cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum());
    }
} 