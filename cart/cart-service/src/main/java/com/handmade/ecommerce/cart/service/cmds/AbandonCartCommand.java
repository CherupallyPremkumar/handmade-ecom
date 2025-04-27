package com.handmade.ecommerce.cart.service.cmds;

import org.chenile.stm.State;
import org.chenile.stm.StateEntity;
import org.chenile.stm.action.STMAction;
import org.springframework.stereotype.Component;
import com.handmade.ecommerce.cart.model.Cart;

@Component
public class AbandonCartCommand implements STMAction<Cart> {
    
    @Override
    public void execute(State<Cart> state) {
        Cart cart = state.getEntity();
        cart.setStatus("ABANDONED");
        // Additional cleanup logic can be added here
    }
} 