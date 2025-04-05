package com.handmade.ecommerce.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.ecommerce.shipping.model.Shipping;


public class CloseShippingAction extends AbstractSTMTransitionAction<Shipping,MinimalPayload>{
	@Override
	public void transitionTo(Shipping shipping, MinimalPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        shipping.closeComment = payload.getComment();
	}

}
