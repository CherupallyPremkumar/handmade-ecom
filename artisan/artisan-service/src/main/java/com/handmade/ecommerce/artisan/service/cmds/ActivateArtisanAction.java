package com.handmade.ecommerce.artisan.service.cmds;

import com.handmade.ecommerce.artisan.dto.payload.ActivateArtisanPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.model.Transition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.handmade.ecommerce.artisan.model.Artisan;
import com.handmade.ecommerce.artisan.service.store.ArtisanEntityStore;

/**
 * Handles the transition of an artisan from VERIFIED to ACTIVE state.
 * This action is triggered when activating a verified artisan.
 */
@Component
public class ActivateArtisanAction implements STMTransitionAction<Artisan> {

    @Override
    public void doTransition(Artisan artisan, Object payload, State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        ActivateArtisanPayload artisanPayload= (ActivateArtisanPayload) payload;
        // Update artisan activation details
        artisan.setActivationDate(java.time.LocalDateTime.now());
        artisan.setActivatedBy(artisanPayload.getActivatedBy());
        artisan.setActivationNotes(artisanPayload.getNotes());

    }

}