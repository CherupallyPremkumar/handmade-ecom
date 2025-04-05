package com.handmade.ecommerce.payment.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

import com.handmade.ecommerce.payment.model.AssignPaymentPayload;
import com.handmade.ecommerce.payment.model.Payment;

/**
    This class implements the assign action. It will need to inherit from the AbstractSTMTransitionAction for
    auto wiring into STM to work properly. Be sure to use the PayloadType as the second argument and
    override the transitionTo method accordingly as shown below.
*/
public class AssignPaymentAction extends AbstractSTMTransitionAction<Payment,AssignPaymentPayload>{

	@Override
	public void transitionTo(Payment payment, AssignPaymentPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
		payment.assignee = payload.assignee;
		payment.assignComment = payload.getComment();
	}

}
