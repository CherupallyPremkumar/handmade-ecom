package com.handmade.ecommerce.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.ecommerce.cart.model.Cart;

public class ResolveCartAction extends AbstractSTMTransitionAction<Cart,MinimalPayload>{

	@Override
	public void transitionTo(Cart cart, MinimalPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
		cart.resolveComment = payload.getComment();
	}

}
