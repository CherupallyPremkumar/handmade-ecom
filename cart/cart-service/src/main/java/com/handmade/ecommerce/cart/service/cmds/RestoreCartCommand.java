package com.handmade.ecommerce.cart.service.cmds;

import org.chenile.stm.State;
import org.chenile.stm.action.STMAction;
import org.springframework.stereotype.Component;
import com.handmade.ecommerce.cart.model.Cart;
import com.handmade.ecommerce.cart.service.exception.CartException;
import com.handmade.ecommerce.cart.service.exception.CartErrorCode;
import org.springframework.http.HttpStatus;

@Component
public class RestoreCartCommand implements STMAction<Cart> {
    
    @Override
    public void execute(State<Cart> state) {
        Cart cart = state.getEntity();
        String currentStatus = cart.getStatus();
        
        // Validate that the cart is in a state that can be restored
        if (!"ABANDONED".equals(currentStatus) && !"EXPIRED".equals(currentStatus)) {
            throw new CartException(
                CartErrorCode.CANNOT_RESTORE_CART, 
                HttpStatus.BAD_REQUEST, 
                new Object[]{currentStatus}
            );
        }
        
        // Restore the cart to ACTIVE state
        cart.setStatus("ACTIVE");
        
        // In a real implementation, you might also:
        // 1. Update the last accessed timestamp
        // 2. Check if items are still available
        // 3. Update prices if they've changed
        // 4. Notify the user that their cart has been restored
        
        System.out.println("Restored cart: " + cart.getId() + " from state: " + currentStatus);
    }
} 