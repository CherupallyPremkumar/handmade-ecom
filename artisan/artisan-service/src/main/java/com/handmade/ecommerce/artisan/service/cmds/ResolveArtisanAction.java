package com.handmade.ecommerce.artisan.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.ecommerce.artisan.model.Artisan;

public class ResolveArtisanAction extends AbstractSTMTransitionAction<Artisan,MinimalPayload>{

	@Override
	public void transitionTo(Artisan artisan, MinimalPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

	}

}
