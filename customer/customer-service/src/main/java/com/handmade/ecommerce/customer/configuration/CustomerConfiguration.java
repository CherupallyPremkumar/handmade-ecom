package com.handmade.ecommerce.customer.configuration;

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
import com.handmade.ecommerce.customer.model.Customer;
import com.handmade.ecommerce.customer.service.cmds.AssignCustomerAction;
import com.handmade.ecommerce.customer.service.cmds.DefaultSTMTransitionAction;
import com.handmade.ecommerce.customer.service.cmds.CloseCustomerAction;
import com.handmade.ecommerce.customer.service.cmds.ResolveCustomerAction;
import com.handmade.ecommerce.customer.service.healthcheck.CustomerHealthChecker;
import com.handmade.ecommerce.customer.service.store.CustomerEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.stmcmds.StmAuthoritiesBuilder;
import java.util.function.Function;
import org.chenile.core.context.ChenileExchange;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class CustomerConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/handmade/ecommerce/customer/customer-states.xml";
	
	@Bean BeanFactoryAdapter customerBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl customerFlowStore(@Qualifier("customerBeanFactoryAdapter") BeanFactoryAdapter customerBeanFactoryAdapter) throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(customerBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean @Autowired STM<Customer> customerEntityStm(@Qualifier("customerFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Customer> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean @Autowired STMActionsInfoProvider customerActionsInfoProvider(@Qualifier("customerFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("customer",provider);
        return provider;
	}
	
	@Bean EntityStore<Customer> customerEntityStore() {
		return new CustomerEntityStore();
	}
	
	@Bean @Autowired StateEntityServiceImpl<Customer> _customerStateEntityService_(
			@Qualifier("customerEntityStm") STM<Customer> stm,
			@Qualifier("customerActionsInfoProvider") STMActionsInfoProvider customerInfoProvider,
			@Qualifier("customerEntityStore") EntityStore<Customer> entityStore){
		return new StateEntityServiceImpl<>(stm, customerInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 
	
	@Bean @Autowired GenericEntryAction<Customer> customerEntryAction(@Qualifier("customerEntityStore") EntityStore<Customer> entityStore,
			@Qualifier("customerActionsInfoProvider") STMActionsInfoProvider customerInfoProvider){
		return new GenericEntryAction<Customer>(entityStore,customerInfoProvider);
	}
	
	@Bean GenericExitAction<Customer> customerExitAction(){
		return new GenericExitAction<Customer>();
	}

	@Bean
	XmlFlowReader customerFlowReader(@Qualifier("customerFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean CustomerHealthChecker customerHealthChecker(){
    	return new CustomerHealthChecker();
    }

    @Bean STMTransitionAction<Customer> defaultcustomerSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver customerTransitionActionResolver(
    @Qualifier("defaultcustomerSTMTransitionAction") STMTransitionAction<Customer> defaultSTMTransitionAction){
        return new STMTransitionActionResolver("customer",defaultSTMTransitionAction);
    }

    @Bean @Autowired StmBodyTypeSelector customerBodyTypeSelector(
    @Qualifier("customerActionsInfoProvider") STMActionsInfoProvider customerInfoProvider,
    @Qualifier("customerTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(customerInfoProvider,stmTransitionActionResolver);
    }

    @Bean @Autowired STMTransitionAction<Customer> customerBaseTransitionAction(
    @Qualifier("customerTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
        return new BaseTransitionAction<>(stmTransitionActionResolver);
    }


    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "customer" + eventId for the method name. (customer is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/handmade/customer/customer-states.xml

    @Bean ResolveCustomerAction customerResolve() {
        return new ResolveCustomerAction();
    }

    @Bean CloseCustomerAction customerClose() {
        return new CloseCustomerAction();
    }

    @Bean AssignCustomerAction customerAssign() {
        return new AssignCustomerAction();
    }


    @Bean @Autowired Function<ChenileExchange, String[]> customerEventAuthoritiesSupplier(
        @Qualifier("customerActionsInfoProvider") STMActionsInfoProvider customerInfoProvider)
                    throws Exception{
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(customerInfoProvider);
        return builder.build();
    }
}
