package com.handmade.ecommerce.cart.service.cmds;

import org.chenile.stm.action.STMAutomaticStateComputation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.handmade.ecommerce.cart.model.Cart;
import com.handmade.ecommerce.order.service.OrderService;
import com.handmade.ecommerce.order.model.Order;
import com.handmade.ecommerce.order.model.OrderItem;
import java.util.stream.Collectors;

@Component
public class CreateOrderFromCartCommand implements STMAutomaticStateComputation<Cart> {

    @Autowired
    private OrderService orderService;

    @Override
    public String execute(Cart cart) throws Exception {
        // Create order from cart
        Order order = orderService.createOrder(cart.getUserId(), cart.getId());
        
        // Convert cart items to order items
        order.setItems(cart.getItems().stream()
                .map(this::convertToOrderItem)
                .collect(Collectors.toList()));
        
        // Set order details
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus("PENDING");
        order.setPaymentStatus("PENDING");
        
        // Save the order
        orderService.updateOrderStatus(order.getId(), "PENDING");
        
        // Update cart status to indicate it's been converted to an order
        cart.setStatus("CONVERTED_TO_ORDER");
        
        return "true";
    }

    private OrderItem convertToOrderItem(com.handmade.ecommerce.cart.model.CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(cartItem.getProductId());
        orderItem.setProductName(cartItem.getProductName());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setUnitPrice(cartItem.getUnitPrice());
        orderItem.setTotalPrice(cartItem.getTotalPrice());
        orderItem.setStatus("PENDING");
        return orderItem;
    }
} 