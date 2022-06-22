package ariadna.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ariadna.services.event.JPAEventsService;
import ariadna.services.event.SpringSQLEventsService;
import ariadna.services.populate.IPopulateService;
import ariadna.services.populate.JPAPopulateService;
import ariadna.services.populate.SpringSQLPopulateService;
import ariadna.services.event.IEventsService;

@Configuration
public class ProfileBasedConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "ariadna-data", value = "type", havingValue = "jpa")
	public IEventsService jpaEventsService() {
		return new JPAEventsService();
	}
	
	@Bean
	@ConditionalOnProperty(prefix = "ariadna-data.type", name = "type", havingValue = "sql")
	public IEventsService sqlEventsService() {
		return new SpringSQLEventsService();
	}

	@Bean
	@ConditionalOnProperty(name = "ariadna-data.type", havingValue = "jpa")
	public IPopulateService jpaPopulateService() {
		return new JPAPopulateService();
	}
	
	@Bean
	@ConditionalOnProperty(name = "ariadna-data.type", havingValue = "sql")
	public IPopulateService sqlPopulateService() {
		return new SpringSQLPopulateService();
	}
}
