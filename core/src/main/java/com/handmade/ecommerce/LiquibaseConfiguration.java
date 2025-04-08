//package com.handmade.ecommerce;
//
//import java.util.Optional;
//import java.util.function.Supplier;
//import javax.sql.DataSource;
//import liquibase.integration.spring.SpringLiquibase;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
//import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
//import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//
//@Configuration
//@EnableConfigurationProperties(value = {DataSourceProperties.class,LiquibaseProperties.class})
//public class LiquibaseConfiguration {
//
//    private final Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);
//
//    private final Environment env;
//
//    public LiquibaseConfiguration(Environment env) {
//        this.env = env;
//    }
//
//    @Bean
//    public SpringLiquibase liquibase(
//            @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource,
//            ObjectProvider<DataSource> dataSource,
//            LiquibaseProperties liquibaseProperties,
//            DataSourceProperties dataSourceProperties
//    ) {
//        SpringLiquibase liquibase= createSpringLiquibase(
//                liquibaseDataSource.getIfAvailable(),
//                liquibaseProperties,
//                dataSource.getIfUnique(),
//                dataSourceProperties
//        );
//
//
//
//        liquibase.setChangeLog("classpath:changelog/changelog-master.xml");
//        liquibase.setContexts(liquibaseProperties.getContexts());
//        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
//        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
//        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
//        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
//        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
//        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
//        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
//        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
//        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
//        liquibase.setShouldRun(true);
//        log.debug("Configuring Liquibase");
//
//        return liquibase;
//    }
//
//    private static SpringLiquibase createSpringLiquibase(DataSource liquibaseDatasource, LiquibaseProperties liquibaseProperties, DataSource dataSource, DataSourceProperties dataSourceProperties) {
//        DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
//        if (liquibaseDataSource != null) {
//            SpringLiquibase liquibase = new SpringLiquibase();
//            liquibase.setDataSource(liquibaseDataSource);
//            return liquibase;
//        } else {
//            SpringLiquibase liquibase = new DataSourceClosingSpringLiquibase();
//            liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
//            return liquibase;
//        }
//
//    }
//
//    private static DataSource getDataSource(DataSource liquibaseDataSource, LiquibaseProperties liquibaseProperties, DataSource dataSource) {
//        if (liquibaseDataSource != null) {
//            return liquibaseDataSource;
//        } else {
//            return liquibaseProperties.getUrl() == null && liquibaseProperties.getUser() == null ? dataSource : null;
//        }
//    }
//
//    private static DataSource createNewDataSource(LiquibaseProperties liquibaseProperties, DataSourceProperties dataSourceProperties) {
//        String url = getProperty(liquibaseProperties::getUrl, dataSourceProperties::determineUrl);
//        String user = getProperty(liquibaseProperties::getUser, dataSourceProperties::determineUsername);
//        String password = getProperty(liquibaseProperties::getPassword, dataSourceProperties::determinePassword);
//        return DataSourceBuilder.create().url(url).username(user).password(password).build();
//    }
//
//    private static String getProperty(Supplier<String> property, Supplier<String> defaultValue) {
//        return Optional.of(property).map(Supplier::get).orElseGet(defaultValue);
//    }
//}
//
