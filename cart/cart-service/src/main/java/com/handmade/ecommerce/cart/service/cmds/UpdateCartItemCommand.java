package com.handmade.ecommerce.cart.service.cmds;

import org.chenile.stm.State;
import org.chenile.stm.StateEntity;
import org.chenile.stm.action.STMAction;
import org.springframework.stereotype.Component;
import com.handmade.ecommerce.cart.model.Cart;
import com.handmade.ecommerce.cart.model.CartItem;
import java.util.Optional;

@Component
public class UpdateCartItemCommand implements STMAction<Cart> {
    
    @Override
    public void execute(State<Cart> state) {
        Cart cart = state.getEntity();
        String itemId = (String) state.getContext().get("itemId");
        Integer quantity = (Integer) state.getContext().get("quantity");
        
        if (itemId == null || quantity == null) {
            throw new IllegalArgumentException("Item ID and quantity cannot be null");
        }
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        
        Optional<CartItem> item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst();
                
        if (item.isPresent()) {
            CartItem cartItem = item.get();
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(quantity * cartItem.getUnitPrice());
            
            // Update cart total
            cart.setTotalAmount(cart.getItems().stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum());
        } else {
            throw new IllegalArgumentException("Item not found in cart");
        }
    }
} 