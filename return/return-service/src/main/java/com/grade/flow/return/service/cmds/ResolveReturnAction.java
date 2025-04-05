package com.handmade.ecommerce.return.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.ecommerce.return.model.Return;

public class ResolveReturnAction extends AbstractSTMTransitionAction<Return,MinimalPayload>{

	@Override
	public void transitionTo(Return return, MinimalPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
		return.resolveComment = payload.getComment();
	}

}
