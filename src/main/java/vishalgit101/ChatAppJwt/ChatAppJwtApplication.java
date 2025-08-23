package vishalgit101.ChatAppJwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"config", "restController", "service"})
@EntityScan(basePackages = "entity")
@EnableJpaRepositories(basePackages = "repo")
public class ChatAppJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatAppJwtApplication.class, args);
	}

}
