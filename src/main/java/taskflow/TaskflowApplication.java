package taskflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import taskflow.config.CorsProperties;

@SpringBootApplication
@EnableConfigurationProperties(CorsProperties.class)
public class TaskflowApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskflowApplication.class, args);
	}
}
