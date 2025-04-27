package com.handmade.ecommerce.artisan.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.handmade.ecommerce.artisan.model.Artisan;
import com.handmade.ecommerce.artisan.service.store.ArtisanEntityStore;
import com.handmade.ecommerce.core.exception.BusinessException;

/**
 * Handles the transition of an artisan from REGISTERED to VERIFIED state.
 * This action is triggered when verifying a newly registered artisan.
 */
@Component
public class VerifyArtisanAction extends AbstractSTMTransitionAction<Artisan, Object> {

    @Autowired
    private ArtisanEntityStore artisanStore;

    @Override
    public void transitionTo(Artisan artisan, Object payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        
        validateTransition(artisan, startState);
        
        // Update artisan verification details
        artisan.setVerificationDate(java.time.LocalDateTime.now());
        artisan.setVerifiedBy(payload.toString()); // Using payload as verifiedBy
        artisan.setStatus(endState.getId());

        // Save updated artisan
        artisanStore.store(artisan);
    }

    private void validateTransition(Artisan artisan, State startState) {
        if (artisan == null) {
            throw new BusinessException("ARTISAN_NOT_FOUND", "Artisan not found");
        }

        if (!startState.getId().equals("REGISTERED")) {
            throw new BusinessException("INVALID_STATE", 
                "Artisan must be in REGISTERED state to be verified. Current state: " + startState.getId());
        }
    }
} 