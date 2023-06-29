package cl.drcde.cqrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "cl.drcde.cqrs.infrastructure.persistence")
@EntityScan(basePackages = "cl.drcde.cqrs.infrastructure.persistence")
@ComponentScan(basePackages = "cl.drcde.cqrs.domain.shared.commandbus")
public class CqrsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CqrsApplication.class, args);
	}

}
