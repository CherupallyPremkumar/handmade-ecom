package com.handmade.ecommerce.artisan.service.cmds;

import com.handmade.ecommerce.artisan.dto.payload.DeactivateArtisanPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.handmade.ecommerce.artisan.model.Artisan;
import com.handmade.ecommerce.artisan.service.store.ArtisanEntityStore;
import com.handmade.ecommerce.core.exception.BusinessException;
import com.handmade.ecommerce.order.service.OrderService;
import com.handmade.ecommerce.notification.service.NotificationService;
import com.handmade.ecommerce.portfolio.service.PortfolioService;

import java.util.List;

/**
 * Handles the transition of an artisan from SUSPENDED to INACTIVE state.
 * This action is triggered when permanently deactivating a suspended artisan.
 */
@Component
public class DeactivateArtisanAction implements STMTransitionAction<Artisan> {




    @Override
    public void doTransition(Artisan artisan, Object payload, State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        DeactivateArtisanPayload deactivateArtisanPayload = (DeactivateArtisanPayload) payload;
        // Update artisan deactivation details
        artisan.setDeactivationDate(java.time.LocalDateTime.now());
        artisan.setDeactivatedBy(deactivateArtisanPayload.toString()); // Using payload as deactivatedBy

    }
}