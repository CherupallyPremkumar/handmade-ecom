package com.handmade.ecommerce.artisan.service.impl;

import com.handmade.ecommerce.artisan.configuration.dao.ArtisanRepository;
import com.handmade.ecommerce.artisan.configuration.dao.ArtisanWorkingHoursRepository;
import com.handmade.ecommerce.artisan.model.Artisan;
import com.handmade.ecommerce.artisan.model.service.ArtisanWorkingHoursService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.model.Transition;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractArtisanWorkingHoursService  implements STMTransitionAction<Artisan> , ArtisanWorkingHoursService {

    protected final ArtisanWorkingHoursRepository workingHoursRepository;
    protected final ArtisanRepository artisanRepository;

    @Override
    public void doTransition(Artisan stateEntity, Object transitionParam, State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

    }

} 