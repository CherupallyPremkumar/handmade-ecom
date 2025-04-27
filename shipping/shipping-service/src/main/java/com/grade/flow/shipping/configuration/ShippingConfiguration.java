package com.grade.flow.shipping.configuration;

import com.grade.flow.shipping.service.cmds.DefaultSTMTransitionAction;
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
import com.handmade.ecommerce.shipping.model.Shipping;
import com.handmade.ecommerce.shipping.service.cmds.AssignShippingAction;
import com.handmade.ecommerce.shipping.service.cmds.CloseShippingAction;
import com.handmade.ecommerce.shipping.service.cmds.ResolveShippingAction;
import com.handmade.ecommerce.shipping.service.healthcheck.ShippingHealthChecker;
import com.handmade.ecommerce.shipping.service.store.ShippingEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.stmcmds.StmAuthoritiesBuilder;
import java.util.function.Function;
import org.chenile.core.context.ChenileExchange;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class ShippingConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/handmade/flow/shipping/shipping-states.xml";
	
	@Bean BeanFactoryAdapter shippingBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl shippingFlowStore(@Qualifier("shippingBeanFactoryAdapter") BeanFactoryAdapter shippingBeanFactoryAdapter) throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(shippingBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean @Autowired STM<Shipping> shippingEntityStm(@Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Shipping> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean @Autowired STMActionsInfoProvider shippingActionsInfoProvider(@Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("shipping",provider);
        return provider;
	}
	
	@Bean EntityStore<Shipping> shippingEntityStore() {
		return new ShippingEntityStore();
	}
	
	@Bean @Autowired StateEntityServiceImpl<Shipping> _shippingStateEntityService_(
			@Qualifier("shippingEntityStm") STM<Shipping> stm,
			@Qualifier("shippingActionsInfoProvider") STMActionsInfoProvider shippingInfoProvider,
			@Qualifier("shippingEntityStore") EntityStore<Shipping> entityStore){
		return new StateEntityServiceImpl<>(stm, shippingInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 
	
	@Bean @Autowired GenericEntryAction<Shipping> shippingEntryAction(@Qualifier("shippingEntityStore") EntityStore<Shipping> entityStore,
			@Qualifier("shippingActionsInfoProvider") STMActionsInfoProvider shippingInfoProvider){
		return new GenericEntryAction<Shipping>(entityStore,shippingInfoProvider);
	}
	
	@Bean GenericExitAction<Shipping> shippingExitAction(){
		return new GenericExitAction<Shipping>();
	}

	@Bean
	XmlFlowReader shippingFlowReader(@Qualifier("shippingFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean ShippingHealthChecker shippingHealthChecker(){
    	return new ShippingHealthChecker();
    }

    @Bean STMTransitionAction<Shipping> defaultshippingSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver shippingTransitionActionResolver(
    @Qualifier("defaultshippingSTMTransitionAction") STMTransitionAction<Shipping> defaultSTMTransitionAction){
        return new STMTransitionActionResolver("shipping",defaultSTMTransitionAction);
    }

    @Bean @Autowired StmBodyTypeSelector shippingBodyTypeSelector(
    @Qualifier("shippingActionsInfoProvider") STMActionsInfoProvider shippingInfoProvider,
    @Qualifier("shippingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(shippingInfoProvider,stmTransitionActionResolver);
    }

    @Bean @Autowired STMTransitionAction<Shipping> shippingBaseTransitionAction(
    @Qualifier("shippingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
        return new BaseTransitionAction<>(stmTransitionActionResolver);
    }


    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "shipping" + eventId for the method name. (shipping is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/handmade/shipping/shipping-states.xml

    @Bean ResolveShippingAction shippingResolve() {
        return new ResolveShippingAction();
    }

    @Bean CloseShippingAction shippingClose() {
        return new CloseShippingAction();
    }

    @Bean AssignShippingAction shippingAssign() {
        return new AssignShippingAction();
    }


    @Bean @Autowired Function<ChenileExchange, String[]> shippingEventAuthoritiesSupplier(
        @Qualifier("shippingActionsInfoProvider") STMActionsInfoProvider shippingInfoProvider)
                    throws Exception{
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(shippingInfoProvider);
        return builder.build();
    }
}
