package com.handmade.ecommerce.return.configuration;

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
import com.handmade.ecommerce.return.model.Return;
import com.handmade.ecommerce.return.service.cmds.AssignReturnAction;
import com.handmade.ecommerce.return.service.cmds.DefaultSTMTransitionAction;
import com.handmade.ecommerce.return.service.cmds.CloseReturnAction;
import com.handmade.ecommerce.return.service.cmds.ResolveReturnAction;
import com.handmade.ecommerce.return.service.healthcheck.ReturnHealthChecker;
import com.handmade.ecommerce.return.service.store.ReturnEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.stmcmds.StmAuthoritiesBuilder;
import java.util.function.Function;
import org.chenile.core.context.ChenileExchange;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class ReturnConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/handmade/flow/return/return-states.xml";
	
	@Bean BeanFactoryAdapter returnBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl returnFlowStore(@Qualifier("returnBeanFactoryAdapter") BeanFactoryAdapter returnBeanFactoryAdapter) throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(returnBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean @Autowired STM<Return> returnEntityStm(@Qualifier("returnFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Return> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean @Autowired STMActionsInfoProvider returnActionsInfoProvider(@Qualifier("returnFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("return",provider);
        return provider;
	}
	
	@Bean EntityStore<Return> returnEntityStore() {
		return new ReturnEntityStore();
	}
	
	@Bean @Autowired StateEntityServiceImpl<Return> _returnStateEntityService_(
			@Qualifier("returnEntityStm") STM<Return> stm,
			@Qualifier("returnActionsInfoProvider") STMActionsInfoProvider returnInfoProvider,
			@Qualifier("returnEntityStore") EntityStore<Return> entityStore){
		return new StateEntityServiceImpl<>(stm, returnInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 
	
	@Bean @Autowired GenericEntryAction<Return> returnEntryAction(@Qualifier("returnEntityStore") EntityStore<Return> entityStore,
			@Qualifier("returnActionsInfoProvider") STMActionsInfoProvider returnInfoProvider){
		return new GenericEntryAction<Return>(entityStore,returnInfoProvider);
	}
	
	@Bean GenericExitAction<Return> returnExitAction(){
		return new GenericExitAction<Return>();
	}

	@Bean
	XmlFlowReader returnFlowReader(@Qualifier("returnFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean ReturnHealthChecker returnHealthChecker(){
    	return new ReturnHealthChecker();
    }

    @Bean STMTransitionAction<Return> defaultreturnSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver returnTransitionActionResolver(
    @Qualifier("defaultreturnSTMTransitionAction") STMTransitionAction<Return> defaultSTMTransitionAction){
        return new STMTransitionActionResolver("return",defaultSTMTransitionAction);
    }

    @Bean @Autowired StmBodyTypeSelector returnBodyTypeSelector(
    @Qualifier("returnActionsInfoProvider") STMActionsInfoProvider returnInfoProvider,
    @Qualifier("returnTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(returnInfoProvider,stmTransitionActionResolver);
    }

    @Bean @Autowired STMTransitionAction<Return> returnBaseTransitionAction(
    @Qualifier("returnTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
        return new BaseTransitionAction<>(stmTransitionActionResolver);
    }


    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "return" + eventId for the method name. (return is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/handmade/return/return-states.xml

    @Bean ResolveReturnAction returnResolve() {
        return new ResolveReturnAction();
    }

    @Bean CloseReturnAction returnClose() {
        return new CloseReturnAction();
    }

    @Bean AssignReturnAction returnAssign() {
        return new AssignReturnAction();
    }


    @Bean @Autowired Function<ChenileExchange, String[]> returnEventAuthoritiesSupplier(
        @Qualifier("returnActionsInfoProvider") STMActionsInfoProvider returnInfoProvider)
                    throws Exception{
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(returnInfoProvider);
        return builder.build();
    }
}
