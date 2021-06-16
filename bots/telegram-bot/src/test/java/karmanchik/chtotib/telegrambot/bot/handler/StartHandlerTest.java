package karmanchik.chtotib.telegrambot.bot.handler;

import karmanchik.chtotib.entityservice.config.DataSourceConfig;
import karmanchik.chtotib.entityservice.repositories.JpaChatUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StartHandlerTest {
    @Autowired
    JpaChatUserRepository userRepository;

    @Test
    public void name() {
        System.out.println(userRepository.findAll());
    }
}