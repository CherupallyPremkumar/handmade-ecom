package com.handmade.ecommerce.artisan.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

import com.handmade.ecommerce.artisan.model.AssignArtisanPayload;
import com.handmade.ecommerce.artisan.model.Artisan;

/**
    This class implements the assign action. It will need to inherit from the AbstractSTMTransitionAction for
    auto wiring into STM to work properly. Be sure to use the PayloadType as the second argument and
    override the transitionTo method accordingly as shown below.
*/
public class AssignArtisanAction extends AbstractSTMTransitionAction<Artisan,AssignArtisanPayload>{

	@Override
	public void transitionTo(Artisan artisan, AssignArtisanPayload payload, State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
	}

}
