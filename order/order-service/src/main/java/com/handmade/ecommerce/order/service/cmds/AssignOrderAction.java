package com.handmade.ecommerce.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

import com.handmade.ecommerce.order.model.AssignOrderPayload;
import com.handmade.ecommerce.order.model.Order;

/**
    This class implements the assign action. It will need to inherit from the AbstractSTMTransitionAction for
    auto wiring into STM to work properly. Be sure to use the PayloadType as the second argument and
    override the transitionTo method accordingly as shown below.
*/
public class AssignOrderAction extends AbstractSTMTransitionAction<Order,AssignOrderPayload>{

	@Override
	public void transitionTo(Order order, AssignOrderPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
		order.assignee = payload.assignee;
		order.assignComment = payload.getComment();
	}

}
