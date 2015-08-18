package com.ericsson.nfvo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ericsson.nfvo.rest._RestPackage;

@ Configuration
@ EnableTransactionManagement
// in org.glassfish.jersey.server.spring.SpringComponentProvider#bind( Class, Set), jersey-spring3 looks for spring-managed beans via
// ctx.getBeanNamesForType(component), which returns an empty String array when dynamic proxy is used. with this annotation we are forcing
// Spring to use CGLib.
@ EnableAspectJAutoProxy( proxyTargetClass = true)
@ ComponentScan( basePackageClasses = { _RestPackage.class})
public class ApplicationContext {

	@ Bean
	public PlatformTransactionManager transactionManager( final EntityManagerFactory entityManagerFactory) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory( entityManagerFactory);
		return transactionManager;
	}

	@ Bean
	public LocalContainerEntityManagerFactoryBean hibernateEntityManagerFactoryBean() {
		OrmFactory ormFactory = new OrmFactory( Nfvo.getDatabaseUrl());
		if( Nfvo.isDevelopmentProfile()) {
			ormFactory.generateDdl();
			ormFactory.showSql();
		}
		return ormFactory;
	}

	@ Bean
	public ApplicationContextInitializer applicationContextInitializer() {
		return new ApplicationContextInitializer();
	}

	@ Bean( destroyMethod = "shutdown")
	public ScheduledExecutorService scheduledExecutorService() {
		return Executors.newSingleThreadScheduledExecutor();
	}
}
