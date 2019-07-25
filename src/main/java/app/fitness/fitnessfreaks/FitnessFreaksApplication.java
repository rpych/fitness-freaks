package app.fitness.fitnessfreaks;

import app.fitness.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableAutoConfiguration
@EnableConfigurationProperties(AppProperties.class)
@ComponentScan(basePackages={"app.fitness"})
@EnableJpaRepositories(basePackages="app.fitness.repositories")
@EnableTransactionManagement
@EntityScan(basePackages="app.fitness.implementations")
public class FitnessFreaksApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitnessFreaksApplication.class, args);
	}

}
