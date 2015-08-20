package com.compareglobal.service.loans.personal;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.helper.ContainsHelper;
import com.github.jknack.handlebars.helper.IsInHelper;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.springmvc.SpringTemplateLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
@ComponentScan
@EnableJpaRepositories({"com.compareglobal.service.loans.personal.repository", "com.compareglobal.service.common.repository"})
@EntityScan({"com.compareglobal.service.loans.personal.domain", "com.compareglobal.service.common.domain"})
@Import({RepositoryRestMvcConfiguration.class})
@EnableAutoConfiguration
@PropertySource("application.properties")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);			
	}

	@Bean
	public Handlebars handlebars(ApplicationContext applicationContext) {
		TemplateLoader loader = new SpringTemplateLoader(applicationContext);
		loader.setPrefix("classpath:/templates");
		loader.setSuffix(".hbs");
		Handlebars handlebars = new Handlebars(loader);

		handlebars = handlebars.registerHelper(IsInHelper.NAME, IsInHelper.INSTANCE)
				.registerHelper(ContainsHelper.NAME, ContainsHelper.INSTANCE);
		return handlebars;
	}

}
