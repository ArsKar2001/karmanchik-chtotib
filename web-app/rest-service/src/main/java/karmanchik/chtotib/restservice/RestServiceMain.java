package karmanchik.chtotib.restservice;

import karmanchik.chtotib.entityservice.enums.Role;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(RestServiceMain.class, args);
        System.out.println(Role.STUDENT);
    }
}
