package com.handmade.ecommerce.artisan.service.cmds;

import com.handmade.ecommerce.artisan.dto.payload.SuspendArtisanPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.model.Transition;
import org.springframework.stereotype.Component;

import com.handmade.ecommerce.artisan.model.Artisan;

/**
 * Handles the transition of an artisan from ACTIVE to SUSPENDED state.
 * This action is triggered when suspending an active artisan.
 */
@Component
public class SuspendArtisanAction implements STMTransitionAction<Artisan> {

    @Override
    public void doTransition(Artisan artisan, Object payload, State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        SuspendArtisanPayload suspendArtisanPayload= (SuspendArtisanPayload) payload;
        artisan.setSuspensionDate(java.time.LocalDateTime.now());
            artisan.setSuspendedBy(suspendArtisanPayload.toString()); // Using payload as suspendedBy

        }
    }