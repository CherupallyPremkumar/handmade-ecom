package com.handmade.ecommerce.artisan.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.handmade.ecommerce.artisan.model.Artisan;
import com.handmade.ecommerce.artisan.service.cmds.AssignArtisanAction;
import com.handmade.ecommerce.artisan.service.cmds.DefaultSTMTransitionAction;
import com.handmade.ecommerce.artisan.service.cmds.CloseArtisanAction;
import com.handmade.ecommerce.artisan.service.cmds.ResolveArtisanAction;
import com.handmade.ecommerce.artisan.service.healthcheck.ArtisanHealthChecker;
import com.handmade.ecommerce.artisan.service.store.ArtisanEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.stmcmds.StmAuthoritiesBuilder;
import java.util.function.Function;
import org.chenile.core.context.ChenileExchange;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class ArtisanConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/handmade/ecommerce/artisan/artisan-states.xml";
	
	@Bean BeanFactoryAdapter artisanBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl artisanFlowStore(@Qualifier("artisanBeanFactoryAdapter") BeanFactoryAdapter artisanBeanFactoryAdapter) throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(artisanBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean @Autowired STM<Artisan> artisanEntityStm(@Qualifier("artisanFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Artisan> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean @Autowired STMActionsInfoProvider artisanActionsInfoProvider(@Qualifier("artisanFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("artisan",provider);
        return provider;
	}
	
	@Bean EntityStore<Artisan> artisanEntityStore() {
		return new ArtisanEntityStore();
	}
	
	@Bean @Autowired StateEntityServiceImpl<Artisan> _artisanStateEntityService_(
			@Qualifier("artisanEntityStm") STM<Artisan> stm,
			@Qualifier("artisanActionsInfoProvider") STMActionsInfoProvider artisanInfoProvider,
			@Qualifier("artisanEntityStore") EntityStore<Artisan> entityStore){
		return new StateEntityServiceImpl<>(stm, artisanInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 
	
	@Bean @Autowired GenericEntryAction<Artisan> artisanEntryAction(@Qualifier("artisanEntityStore") EntityStore<Artisan> entityStore,
			@Qualifier("artisanActionsInfoProvider") STMActionsInfoProvider artisanInfoProvider){
		return new GenericEntryAction<Artisan>(entityStore,artisanInfoProvider);
	}
	
	@Bean GenericExitAction<Artisan> artisanExitAction(){
		return new GenericExitAction<Artisan>();
	}

	@Bean
	XmlFlowReader artisanFlowReader(@Qualifier("artisanFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean ArtisanHealthChecker artisanHealthChecker(){
    	return new ArtisanHealthChecker();
    }

    @Bean STMTransitionAction<Artisan> defaultartisanSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver artisanTransitionActionResolver(
    @Qualifier("defaultartisanSTMTransitionAction") STMTransitionAction<Artisan> defaultSTMTransitionAction){
        return new STMTransitionActionResolver("artisan",defaultSTMTransitionAction);
    }

    @Bean @Autowired StmBodyTypeSelector artisanBodyTypeSelector(
    @Qualifier("artisanActionsInfoProvider") STMActionsInfoProvider artisanInfoProvider,
    @Qualifier("artisanTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(artisanInfoProvider,stmTransitionActionResolver);
    }

    @Bean @Autowired STMTransitionAction<Artisan> artisanBaseTransitionAction(
    @Qualifier("artisanTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
        return new BaseTransitionAction<>(stmTransitionActionResolver);
    }


    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "artisan" + eventId for the method name. (artisan is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/handmade/artisan/artisan-states.xml

    @Bean ResolveArtisanAction artisanResolve() {
        return new ResolveArtisanAction();
    }

    @Bean CloseArtisanAction artisanClose() {
        return new CloseArtisanAction();
    }

    @Bean AssignArtisanAction artisanAssign() {
        return new AssignArtisanAction();
    }


    @Bean @Autowired Function<ChenileExchange, String[]> artisanEventAuthoritiesSupplier(
        @Qualifier("artisanActionsInfoProvider") STMActionsInfoProvider artisanInfoProvider)
                    throws Exception{
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(artisanInfoProvider);
        return builder.build();
    }
}
