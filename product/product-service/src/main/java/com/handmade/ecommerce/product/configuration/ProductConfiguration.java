package com.handmade.ecommerce.product.configuration;

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
import com.handmade.ecommerce.product.model.Product;
import com.handmade.ecommerce.product.service.cmds.DefaultSTMTransitionAction;
import com.handmade.ecommerce.product.service.healthcheck.ProductHealthChecker;
import com.handmade.ecommerce.product.service.store.ProductEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.stmcmds.StmAuthoritiesBuilder;
import java.util.function.Function;
import org.chenile.core.context.ChenileExchange;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class ProductConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/handmade/ecommerce/product/product-states.xml";
	
	@Bean BeanFactoryAdapter productBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl productFlowStore(@Qualifier("productBeanFactoryAdapter") BeanFactoryAdapter productBeanFactoryAdapter) throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(productBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean @Autowired STM<Product> productEntityStm(@Qualifier("productFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Product> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean @Autowired STMActionsInfoProvider productActionsInfoProvider(@Qualifier("productFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("ProductFlow",provider);
        return provider;
	}
	
	@Bean EntityStore<Product> productEntityStore() {
		return new ProductEntityStore();
	}
	
	@Bean @Autowired StateEntityServiceImpl<Product> _productStateEntityService_(
			@Qualifier("productEntityStm") STM<Product> stm,
			@Qualifier("productActionsInfoProvider") STMActionsInfoProvider productInfoProvider,
			@Qualifier("productEntityStore") EntityStore<Product> entityStore){
		return new StateEntityServiceImpl<>(stm, productInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 
	
	@Bean @Autowired GenericEntryAction<Product> productEntryAction(@Qualifier("productEntityStore") EntityStore<Product> entityStore,
			@Qualifier("productActionsInfoProvider") STMActionsInfoProvider productInfoProvider){
		return new GenericEntryAction<Product>(entityStore,productInfoProvider);
	}
	
	@Bean GenericExitAction<Product> productExitAction(){
		return new GenericExitAction<Product>();
	}

	@Bean
	XmlFlowReader productFlowReader(@Qualifier("productFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean ProductHealthChecker productHealthChecker(){
    	return new ProductHealthChecker();
    }

    @Bean STMTransitionAction<Product> defaultproductSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver productTransitionActionResolver(
    @Qualifier("defaultproductSTMTransitionAction") STMTransitionAction<Product> defaultSTMTransitionAction){
        return new STMTransitionActionResolver("product",defaultSTMTransitionAction);
    }

    @Bean @Autowired StmBodyTypeSelector productBodyTypeSelector(
    @Qualifier("productActionsInfoProvider") STMActionsInfoProvider productInfoProvider,
    @Qualifier("productTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(productInfoProvider,stmTransitionActionResolver);
    }

    @Bean @Autowired STMTransitionAction<Product> productBaseTransitionAction(
    @Qualifier("productTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
        return new BaseTransitionAction<>(stmTransitionActionResolver);
    }


    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "product" + eventId for the method name. (product is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/handmade/product/product-states.xml




    @Bean @Autowired Function<ChenileExchange, String[]> productEventAuthoritiesSupplier(
        @Qualifier("productActionsInfoProvider") STMActionsInfoProvider productInfoProvider)
                    throws Exception{
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(productInfoProvider);
        return builder.build();
    }
}
