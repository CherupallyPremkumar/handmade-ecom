package com.handmade.ecommerce.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

import com.handmade.ecommerce.cart.model.AssignCartPayload;
import com.handmade.ecommerce.cart.model.Cart;

/**
    This class implements the assign action. It will need to inherit from the AbstractSTMTransitionAction for
    auto wiring into STM to work properly. Be sure to use the PayloadType as the second argument and
    override the transitionTo method accordingly as shown below.
*/
public class AssignCartAction extends AbstractSTMTransitionAction<Cart,AssignCartPayload>{

	@Override
	public void transitionTo(Cart cart, AssignCartPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
		cart.assignee = payload.assignee;
		cart.assignComment = payload.getComment();
	}

}
