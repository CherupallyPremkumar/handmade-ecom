package com.handmade.ecommerce.payment.configuration;

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
import com.handmade.ecommerce.payment.model.Payment;
import com.handmade.ecommerce.payment.service.cmds.AssignPaymentAction;
import com.handmade.ecommerce.payment.service.cmds.DefaultSTMTransitionAction;
import com.handmade.ecommerce.payment.service.cmds.ClosePaymentAction;
import com.handmade.ecommerce.payment.service.cmds.ResolvePaymentAction;
import com.handmade.ecommerce.payment.service.healthcheck.PaymentHealthChecker;
import com.handmade.ecommerce.payment.service.store.PaymentEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.stmcmds.StmAuthoritiesBuilder;
import java.util.function.Function;
import org.chenile.core.context.ChenileExchange;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class PaymentConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/handmade/flow/payment/payment-states.xml";
	
	@Bean BeanFactoryAdapter paymentBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl paymentFlowStore(@Qualifier("paymentBeanFactoryAdapter") BeanFactoryAdapter paymentBeanFactoryAdapter) throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(paymentBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean @Autowired STM<Payment> paymentEntityStm(@Qualifier("paymentFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Payment> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean @Autowired STMActionsInfoProvider paymentActionsInfoProvider(@Qualifier("paymentFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("payment",provider);
        return provider;
	}
	
	@Bean EntityStore<Payment> paymentEntityStore() {
		return new PaymentEntityStore();
	}
	
	@Bean @Autowired StateEntityServiceImpl<Payment> _paymentStateEntityService_(
			@Qualifier("paymentEntityStm") STM<Payment> stm,
			@Qualifier("paymentActionsInfoProvider") STMActionsInfoProvider paymentInfoProvider,
			@Qualifier("paymentEntityStore") EntityStore<Payment> entityStore){
		return new StateEntityServiceImpl<>(stm, paymentInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 
	
	@Bean @Autowired GenericEntryAction<Payment> paymentEntryAction(@Qualifier("paymentEntityStore") EntityStore<Payment> entityStore,
			@Qualifier("paymentActionsInfoProvider") STMActionsInfoProvider paymentInfoProvider){
		return new GenericEntryAction<Payment>(entityStore,paymentInfoProvider);
	}
	
	@Bean GenericExitAction<Payment> paymentExitAction(){
		return new GenericExitAction<Payment>();
	}

	@Bean
	XmlFlowReader paymentFlowReader(@Qualifier("paymentFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean PaymentHealthChecker paymentHealthChecker(){
    	return new PaymentHealthChecker();
    }

    @Bean STMTransitionAction<Payment> defaultpaymentSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver paymentTransitionActionResolver(
    @Qualifier("defaultpaymentSTMTransitionAction") STMTransitionAction<Payment> defaultSTMTransitionAction){
        return new STMTransitionActionResolver("payment",defaultSTMTransitionAction);
    }

    @Bean @Autowired StmBodyTypeSelector paymentBodyTypeSelector(
    @Qualifier("paymentActionsInfoProvider") STMActionsInfoProvider paymentInfoProvider,
    @Qualifier("paymentTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(paymentInfoProvider,stmTransitionActionResolver);
    }

    @Bean @Autowired STMTransitionAction<Payment> paymentBaseTransitionAction(
    @Qualifier("paymentTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
        return new BaseTransitionAction<>(stmTransitionActionResolver);
    }


    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "payment" + eventId for the method name. (payment is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/handmade/payment/payment-states.xml

    @Bean ResolvePaymentAction paymentResolve() {
        return new ResolvePaymentAction();
    }

    @Bean ClosePaymentAction paymentClose() {
        return new ClosePaymentAction();
    }

    @Bean AssignPaymentAction paymentAssign() {
        return new AssignPaymentAction();
    }


    @Bean @Autowired Function<ChenileExchange, String[]> paymentEventAuthoritiesSupplier(
        @Qualifier("paymentActionsInfoProvider") STMActionsInfoProvider paymentInfoProvider)
                    throws Exception{
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(paymentInfoProvider);
        return builder.build();
    }
}
