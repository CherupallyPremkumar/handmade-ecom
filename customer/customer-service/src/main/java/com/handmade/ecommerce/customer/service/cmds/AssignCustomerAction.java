package com.handmade.ecommerce.customer.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

import com.handmade.ecommerce.customer.model.AssignCustomerPayload;
import com.handmade.ecommerce.customer.model.Customer;

/**
    This class implements the assign action. It will need to inherit from the AbstractSTMTransitionAction for
    auto wiring into STM to work properly. Be sure to use the PayloadType as the second argument and
    override the transitionTo method accordingly as shown below.
*/
public class AssignCustomerAction extends AbstractSTMTransitionAction<Customer,AssignCustomerPayload>{

	@Override
	public void transitionTo(Customer customer, AssignCustomerPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
		customer.assignee = payload.assignee;
		customer.assignComment = payload.getComment();
	}

}
