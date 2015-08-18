package com.ericsson.nfvo;

import java.util.Map;
import java.util.Properties;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class GenerateDdl {

	public static void main( final String[] args) throws InterruptedException {
		try( final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext( OrmFactory.class)) {
			final LocalContainerEntityManagerFactoryBean emfb = context.getBean( LocalContainerEntityManagerFactoryBean.class);
			final Properties jpaProperties = new Properties();
			for( final Map.Entry< String, ?> p: emfb.getJpaPropertyMap().entrySet())
				if( p.getValue() instanceof String) jpaProperties.setProperty( p.getKey(), ( String) p.getValue());
			jpaProperties.setProperty( "javax.persistence.schema-generation.create-source", "metadata");
			jpaProperties.setProperty( "javax.persistence.schema-generation.database.action", "none");
			jpaProperties.setProperty( "javax.persistence.schema-generation.scripts.action", "drop-and-create");
			jpaProperties.setProperty( "javax.persistence.schema-generation.scripts.drop-target", "C:\\local\\tmp\\jpa\\drop.sql");
			jpaProperties.setProperty( "javax.persistence.schema-generation.scripts.create-target", "C:\\local\\tmp\\jpa\\create.sql");
			emfb.getJpaVendorAdapter().getPersistenceProvider().generateSchema( emfb.getPersistenceUnitInfo(), jpaProperties);
		}
	}
}
