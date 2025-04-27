package com.handmade.ecommerce.tenant.configuration;

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
import com.handmade.ecommerce.tenant.model.Tenant;
import com.handmade.ecommerce.tenant.service.cmds.AssignTenantAction;
import com.handmade.ecommerce.tenant.service.cmds.DefaultSTMTransitionAction;
import com.handmade.ecommerce.tenant.service.cmds.CloseTenantAction;
import com.handmade.ecommerce.tenant.service.cmds.ResolveTenantAction;
import com.handmade.ecommerce.tenant.service.healthcheck.TenantHealthChecker;
import com.handmade.ecommerce.tenant.service.store.TenantEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.stmcmds.StmAuthoritiesBuilder;
import java.util.function.Function;
import org.chenile.core.context.ChenileExchange;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class TenantConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/handmade/ecommerce/tenant/tenant-states.xml";
	
	@Bean BeanFactoryAdapter tenantBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl tenantFlowStore(@Qualifier("tenantBeanFactoryAdapter") BeanFactoryAdapter tenantBeanFactoryAdapter) throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(tenantBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean @Autowired STM<Tenant> tenantEntityStm(@Qualifier("tenantFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Tenant> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean @Autowired STMActionsInfoProvider tenantActionsInfoProvider(@Qualifier("tenantFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("tenant",provider);
        return provider;
	}
	
	@Bean EntityStore<Tenant> tenantEntityStore() {
		return new TenantEntityStore();
	}
	
	@Bean @Autowired StateEntityServiceImpl<Tenant> _tenantStateEntityService_(
			@Qualifier("tenantEntityStm") STM<Tenant> stm,
			@Qualifier("tenantActionsInfoProvider") STMActionsInfoProvider tenantInfoProvider,
			@Qualifier("tenantEntityStore") EntityStore<Tenant> entityStore){
		return new StateEntityServiceImpl<>(stm, tenantInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 
	
	@Bean @Autowired GenericEntryAction<Tenant> tenantEntryAction(@Qualifier("tenantEntityStore") EntityStore<Tenant> entityStore,
			@Qualifier("tenantActionsInfoProvider") STMActionsInfoProvider tenantInfoProvider){
		return new GenericEntryAction<Tenant>(entityStore,tenantInfoProvider);
	}
	
	@Bean GenericExitAction<Tenant> tenantExitAction(){
		return new GenericExitAction<Tenant>();
	}


	@Bean
	XmlFlowReader tenantFlowReader(@Qualifier("tenantFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean TenantHealthChecker tenantHealthChecker(){
    	return new TenantHealthChecker();
    }

    @Bean STMTransitionAction<Tenant> defaulttenantSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver tenantTransitionActionResolver(
    @Qualifier("defaulttenantSTMTransitionAction") STMTransitionAction<Tenant> defaultSTMTransitionAction){
        return new STMTransitionActionResolver("tenant",defaultSTMTransitionAction);
    }

    @Bean @Autowired StmBodyTypeSelector tenantBodyTypeSelector(
    @Qualifier("tenantActionsInfoProvider") STMActionsInfoProvider tenantInfoProvider,
    @Qualifier("tenantTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(tenantInfoProvider,stmTransitionActionResolver);
    }

    @Bean @Autowired STMTransitionAction<Tenant> tenantBaseTransitionAction(
    @Qualifier("tenantTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
        return new BaseTransitionAction<>(stmTransitionActionResolver);
    }


    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "tenant" + eventId for the method name. (tenant is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/handmade/tenant/tenant-states.xml

    @Bean ResolveTenantAction tenantResolve() {
        return new ResolveTenantAction();
    }

    @Bean CloseTenantAction tenantClose() {
        return new CloseTenantAction();
    }

    @Bean AssignTenantAction tenantAssign() {
        return new AssignTenantAction();
    }


    @Bean @Autowired Function<ChenileExchange, String[]> tenantEventAuthoritiesSupplier(
        @Qualifier("tenantActionsInfoProvider") STMActionsInfoProvider tenantInfoProvider)
                    throws Exception{
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(tenantInfoProvider);
        return builder.build();
    }
}
