package karmanchik.chtotib.webapp.config;

import karmanchik.chtotib.models.config.DataSourceConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * Класс-пользовательских настроек для Rest API-клиента
 */
@Log4j2
@Configuration
@Import(value = {DataSourceConfig.class})
@EnableTransactionManagement
public class RestConfiguration implements WebMvcConfigurer {

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
