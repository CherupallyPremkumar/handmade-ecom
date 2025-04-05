package com.handmade.ecommerce.order.configuration;

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
import com.handmade.ecommerce.order.model.Order;
import com.handmade.ecommerce.order.service.cmds.AssignOrderAction;
import com.handmade.ecommerce.order.service.cmds.DefaultSTMTransitionAction;
import com.handmade.ecommerce.order.service.cmds.CloseOrderAction;
import com.handmade.ecommerce.order.service.cmds.ResolveOrderAction;
import com.handmade.ecommerce.order.service.healthcheck.OrderHealthChecker;
import com.handmade.ecommerce.order.service.store.OrderEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.stmcmds.StmAuthoritiesBuilder;
import java.util.function.Function;
import org.chenile.core.context.ChenileExchange;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class OrderConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/handmade/ecommerce/order/order-states.xml";
	
	@Bean BeanFactoryAdapter orderBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl orderFlowStore(@Qualifier("orderBeanFactoryAdapter") BeanFactoryAdapter orderBeanFactoryAdapter) throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(orderBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean @Autowired STM<Order> orderEntityStm(@Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Order> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean @Autowired STMActionsInfoProvider orderActionsInfoProvider(@Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("order",provider);
        return provider;
	}
	
	@Bean EntityStore<Order> orderEntityStore() {
		return new OrderEntityStore();
	}
	
	@Bean @Autowired StateEntityServiceImpl<Order> _orderStateEntityService_(
			@Qualifier("orderEntityStm") STM<Order> stm,
			@Qualifier("orderActionsInfoProvider") STMActionsInfoProvider orderInfoProvider,
			@Qualifier("orderEntityStore") EntityStore<Order> entityStore){
		return new StateEntityServiceImpl<>(stm, orderInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 
	
	@Bean @Autowired GenericEntryAction<Order> orderEntryAction(@Qualifier("orderEntityStore") EntityStore<Order> entityStore,
			@Qualifier("orderActionsInfoProvider") STMActionsInfoProvider orderInfoProvider){
		return new GenericEntryAction<Order>(entityStore,orderInfoProvider);
	}
	
	@Bean GenericExitAction<Order> orderExitAction(){
		return new GenericExitAction<Order>();
	}

	@Bean
	XmlFlowReader orderFlowReader(@Qualifier("orderFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean OrderHealthChecker orderHealthChecker(){
    	return new OrderHealthChecker();
    }

    @Bean STMTransitionAction<Order> defaultorderSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver orderTransitionActionResolver(
    @Qualifier("defaultorderSTMTransitionAction") STMTransitionAction<Order> defaultSTMTransitionAction){
        return new STMTransitionActionResolver("order",defaultSTMTransitionAction);
    }

    @Bean @Autowired StmBodyTypeSelector orderBodyTypeSelector(
    @Qualifier("orderActionsInfoProvider") STMActionsInfoProvider orderInfoProvider,
    @Qualifier("orderTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(orderInfoProvider,stmTransitionActionResolver);
    }

    @Bean @Autowired STMTransitionAction<Order> orderBaseTransitionAction(
    @Qualifier("orderTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
        return new BaseTransitionAction<>(stmTransitionActionResolver);
    }


    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "order" + eventId for the method name. (order is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/handmade/order/order-states.xml

    @Bean ResolveOrderAction orderResolve() {
        return new ResolveOrderAction();
    }

    @Bean CloseOrderAction orderClose() {
        return new CloseOrderAction();
    }

    @Bean AssignOrderAction orderAssign() {
        return new AssignOrderAction();
    }


    @Bean @Autowired Function<ChenileExchange, String[]> orderEventAuthoritiesSupplier(
        @Qualifier("orderActionsInfoProvider") STMActionsInfoProvider orderInfoProvider)
                    throws Exception{
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(orderInfoProvider);
        return builder.build();
    }
}
