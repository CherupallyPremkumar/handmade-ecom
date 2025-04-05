package com.handmade.ecommerce.payment.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.ecommerce.payment.model.Payment;


public class ClosePaymentAction extends AbstractSTMTransitionAction<Payment,MinimalPayload>{
	@Override
	public void transitionTo(Payment payment, MinimalPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        payment.closeComment = payload.getComment();
	}

}
