package com.handmade.ecommerce.cart.configuration;

import com.handmade.ecommerce.cart.configuration.dao.CartRepository;
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
import com.handmade.ecommerce.cart.model.Cart;
import com.handmade.ecommerce.cart.service.cmds.AssignCartAction;
import com.handmade.ecommerce.cart.service.cmds.DefaultSTMTransitionAction;
import com.handmade.ecommerce.cart.service.cmds.CloseCartAction;
import com.handmade.ecommerce.cart.service.cmds.ResolveCartAction;
import com.handmade.ecommerce.cart.service.healthcheck.CartHealthChecker;
import com.handmade.ecommerce.cart.service.store.CartEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.stmcmds.StmAuthoritiesBuilder;
import java.util.function.Function;
import org.chenile.core.context.ChenileExchange;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class CartConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/handmade/ecommerce/cart/cart-states.xml";
	
	@Bean BeanFactoryAdapter cartBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl cartFlowStore(@Qualifier("cartBeanFactoryAdapter") BeanFactoryAdapter cartBeanFactoryAdapter) throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(cartBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean @Autowired STM<Cart> cartEntityStm(@Qualifier("cartFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Cart> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean @Autowired STMActionsInfoProvider cartActionsInfoProvider(@Qualifier("cartFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("cart",provider);
        return provider;
	}


	@Bean EntityStore<Cart> cartEntityStore() {
		return new CartEntityStore();
	}
	
	@Bean @Autowired StateEntityServiceImpl<Cart> _cartStateEntityService_(
			@Qualifier("cartEntityStm") STM<Cart> stm,
			@Qualifier("cartActionsInfoProvider") STMActionsInfoProvider cartInfoProvider,
			@Qualifier("cartEntityStore") EntityStore<Cart> entityStore){
		return new StateEntityServiceImpl<>(stm, cartInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 
	
	@Bean @Autowired GenericEntryAction<Cart> cartEntryAction(@Qualifier("cartEntityStore") EntityStore<Cart> entityStore,
			@Qualifier("cartActionsInfoProvider") STMActionsInfoProvider cartInfoProvider){
		return new GenericEntryAction<Cart>(entityStore,cartInfoProvider);
	}
	
	@Bean GenericExitAction<Cart> cartExitAction(){
		return new GenericExitAction<Cart>();
	}

	@Bean
	XmlFlowReader cartFlowReader(@Qualifier("cartFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean CartHealthChecker cartHealthChecker(){
    	return new CartHealthChecker();
    }

    @Bean STMTransitionAction<Cart> defaultcartSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver cartTransitionActionResolver(
    @Qualifier("defaultcartSTMTransitionAction") STMTransitionAction<Cart> defaultSTMTransitionAction){
        return new STMTransitionActionResolver("cart",defaultSTMTransitionAction);
    }

    @Bean @Autowired StmBodyTypeSelector cartBodyTypeSelector(
    @Qualifier("cartActionsInfoProvider") STMActionsInfoProvider cartInfoProvider,
    @Qualifier("cartTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(cartInfoProvider,stmTransitionActionResolver);
    }

    @Bean @Autowired STMTransitionAction<Cart> cartBaseTransitionAction(
    @Qualifier("cartTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
        return new BaseTransitionAction<>(stmTransitionActionResolver);
    }


    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "cart" + eventId for the method name. (cart is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/handmade/cart/cart-states.xml

    @Bean ResolveCartAction cartResolve() {
        return new ResolveCartAction();
    }

    @Bean CloseCartAction cartClose() {
        return new CloseCartAction();
    }

    @Bean AssignCartAction cartAssign() {
        return new AssignCartAction();
    }


    @Bean @Autowired Function<ChenileExchange, String[]> cartEventAuthoritiesSupplier(
        @Qualifier("cartActionsInfoProvider") STMActionsInfoProvider cartInfoProvider)
                    throws Exception{
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(cartInfoProvider);
        return builder.build();
    }
}
