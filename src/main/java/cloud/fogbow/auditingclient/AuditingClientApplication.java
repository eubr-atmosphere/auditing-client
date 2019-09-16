package cloud.fogbow.auditingclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;


@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
public class AuditingClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuditingClientApplication.class, args);
	}

}
