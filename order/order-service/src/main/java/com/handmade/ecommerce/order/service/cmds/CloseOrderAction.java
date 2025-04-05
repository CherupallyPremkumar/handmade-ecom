package com.handmade.ecommerce.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.ecommerce.order.model.Order;


public class CloseOrderAction extends AbstractSTMTransitionAction<Order,MinimalPayload>{
	@Override
	public void transitionTo(Order order, MinimalPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        order.closeComment = payload.getComment();
	}

}
