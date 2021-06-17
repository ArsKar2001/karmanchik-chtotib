package karmanchik.chtotib.models.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "karmanchik/chtotib/models/entity")
@EnableJpaRepositories(basePackages = "karmanchik.chtotib.models.repositories")
public class DataSourceConfig {

}
