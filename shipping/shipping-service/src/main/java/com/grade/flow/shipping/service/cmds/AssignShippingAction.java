package com.handmade.ecommerce.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

import com.handmade.ecommerce.shipping.model.AssignShippingPayload;
import com.handmade.ecommerce.shipping.model.Shipping;

/**
    This class implements the assign action. It will need to inherit from the AbstractSTMTransitionAction for
    auto wiring into STM to work properly. Be sure to use the PayloadType as the second argument and
    override the transitionTo method accordingly as shown below.
*/
public class AssignShippingAction extends AbstractSTMTransitionAction<Shipping,AssignShippingPayload>{

	@Override
	public void transitionTo(Shipping shipping, AssignShippingPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
		shipping.assignee = payload.assignee;
		shipping.assignComment = payload.getComment();
	}

}
