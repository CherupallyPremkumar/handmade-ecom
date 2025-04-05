package com.handmade.ecommerce.customer.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.ecommerce.customer.model.Customer;


public class CloseCustomerAction extends AbstractSTMTransitionAction<Customer,MinimalPayload>{
	@Override
	public void transitionTo(Customer customer, MinimalPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        customer.closeComment = payload.getComment();
	}

}
