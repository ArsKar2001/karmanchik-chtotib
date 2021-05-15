package karmanchik.chtotib.restservice.config;

import karmanchik.chtotib.entityservice.config.DataSourceConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Log4j2
@Configuration
@Import(value = {DataSourceConfig.class})
@EnableTransactionManagement
public class MyConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Create WebMvcConfigurer");
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("PUT", "DELETE", "GET", "POST")
                .allowedHeaders("Accept", "Content-Type", "Host", "Age")
                .exposedHeaders("*")
                .allowCredentials(false).maxAge(3600);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Irkutsk"));
    }
}
