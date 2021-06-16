package karmanchik.chtotib.entityservice.repositories;

import karmanchik.chtotib.entityservice.EntityServiceMain;
import karmanchik.chtotib.entityservice.config.DataSourceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaChatUserRepositoryTest {
    @Autowired
    JpaChatUserRepository userRepository;

    @Test
    public void testName1() {
        System.out.println(userRepository.findAll());
    }
}