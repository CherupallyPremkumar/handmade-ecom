package com.handmade.ecommerce.artisan.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.model.Transition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.handmade.ecommerce.artisan.model.Artisan;
import com.handmade.ecommerce.artisan.service.store.ArtisanEntityStore;
import org.springframework.stereotype.Service;


/**
 * Handles the transition of an artisan from REGISTERED to VERIFIED state.
 * This action is triggered when verifying a newly registered artisan.
 */
@Service
public class VerifyArtisanAction implements STMTransitionAction<Artisan> {


    private final ArtisanEntityStore artisanEntityStore;
    @Autowired
    public VerifyArtisanAction(@Qualifier("artisanEntityStore") ArtisanEntityStore artisanEntityStore) {
        this.artisanEntityStore = artisanEntityStore;
    }

    @Override
    public void doTransition(Artisan stateEntity, Object transitionParam, State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

    }
}