package com.handmade.ecommerce.return.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

import com.handmade.ecommerce.return.model.AssignReturnPayload;
import com.handmade.ecommerce.return.model.Return;

/**
    This class implements the assign action. It will need to inherit from the AbstractSTMTransitionAction for
    auto wiring into STM to work properly. Be sure to use the PayloadType as the second argument and
    override the transitionTo method accordingly as shown below.
*/
public class AssignReturnAction extends AbstractSTMTransitionAction<Return,AssignReturnPayload>{

	@Override
	public void transitionTo(Return return, AssignReturnPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
		return.assignee = payload.assignee;
		return.assignComment = payload.getComment();
	}

}
