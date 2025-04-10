package com.handmade.ecommerce.tenant.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.ecommerce.tenant.model.Tenant;


public class CloseTenantAction extends AbstractSTMTransitionAction<Tenant,MinimalPayload>{
	@Override
	public void transitionTo(Tenant tenant, MinimalPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

	}

}
