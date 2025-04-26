package com.handmade.ecommerce.product.service.cmds;

import com.handmade.ecommerce.product.model.Product;
import org.chenile.stm.action.STMAutomaticStateComputation;
import org.springframework.stereotype.Component;

@Component
public class ValidateProductStockService implements STMAutomaticStateComputation<Product> {

    @Override
    public String execute(Product stateEntity) throws Exception {
        return "true";
    }
}
